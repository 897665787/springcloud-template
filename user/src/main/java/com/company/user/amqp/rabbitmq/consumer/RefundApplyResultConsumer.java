package com.company.user.amqp.rabbitmq.consumer;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.function.Consumer;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.company.common.util.JsonUtil;
import com.company.common.util.Utils;
import com.company.framework.amqp.rabbit.constants.FanoutConstants;
import com.company.framework.amqp.rabbit.utils.ConsumerUtils;
import com.company.framework.sequence.SequenceGenerator;
import com.company.order.api.enums.OrderEnum;
import com.company.order.api.enums.PayRefundApplyEnum;
import com.company.order.api.feign.OrderFeign;
import com.company.order.api.feign.RefundApplyFeign;
import com.company.order.api.request.OrderRefundFailReq;
import com.company.order.api.request.OrderRefundFinishReq;
import com.company.order.api.request.PayRefundApplyReq;
import com.company.order.api.response.Order4Resp;
import com.company.user.api.enums.WalletEnum;
import com.company.user.api.enums.WalletEnum.Type;
import com.company.user.entity.MemberBuyOrder;
import com.company.user.entity.RechargeOrder;
import com.company.user.service.MemberBuyOrderService;
import com.company.user.service.RechargeOrderService;
import com.company.user.service.WalletUseSeqService;
import com.company.user.service.market.UserCouponService;
import com.company.user.wallet.IWallet;
import com.company.user.wallet.dto.FreezeMainChargeGiftAmount;
import com.company.user.wallet.dto.FreezeMainChargeGiftBalance;
import com.company.user.wallet.dto.MainChargeGiftAmount;
import com.company.user.wallet.dto.MainChargeGiftBalance;
import com.company.user.wallet.dto.MainChargeGiftWalletId;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.rabbitmq.client.Channel;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class RefundApplyResultConsumer {

	@Autowired
	private MemberBuyOrderService memberBuyOrderService;
	@Autowired
	private UserCouponService userCouponService;
	@Autowired
	private OrderFeign orderFeign;
	
	@Autowired
	private RechargeOrderService rechargeOrderService;
	@Autowired
	private IWallet<MainChargeGiftWalletId, MainChargeGiftAmount, MainChargeGiftAmount, MainChargeGiftBalance> mainChargeGiftWallet;
	@Autowired
	private IWallet<MainChargeGiftWalletId, MainChargeGiftAmount, FreezeMainChargeGiftAmount, FreezeMainChargeGiftBalance> freezeMainChargeGiftWallet;
	@Autowired
	private WalletUseSeqService walletUseSeqService;
	@Autowired
	private RefundApplyFeign refundApplyFeign;
	@Autowired
	private SequenceGenerator sequenceGenerator;

	@RabbitListener(bindings = @QueueBinding(value = @Queue(value = FanoutConstants.REFUND_APPLY_RESULT.MEMBER_BUY_QUEUE, durable = "false", autoDelete = "true"), exchange = @Exchange(value = FanoutConstants.REFUND_APPLY_RESULT.EXCHANGE, type = ExchangeTypes.FANOUT, durable = "false", autoDelete = "true")))
	public void memberBuy(String jsonStrMsg, Channel channel, Message message) {
		ConsumerUtils.handleByConsumer(jsonStrMsg, channel, message, new Consumer<Map<String, Object>>() {
			@Override
			public void accept(Map<String, Object> params) {
				String orderCode = MapUtils.getString(params, "orderCode");
				// 根据orderCode查询会员订单，查不到的情况下直接退出
				MemberBuyOrder memberBuyOrder = memberBuyOrderService.selectByOrderCode(orderCode);
				if (memberBuyOrder == null) {
					log.info("不是购买会员订单");
					return;
				}
				
				// 处理会员订单逻辑
				Boolean success = MapUtils.getBoolean(params, "success");
				if (success) {// 退款成功
					BigDecimal walletRefundAmount = BigDecimal.ZERO;
					String attach = MapUtils.getString(params, "attach");
					String attachStr = Utils.getByJson(attach, "attach");
					String walletRefundAmountStr = Utils.getByJson(attachStr, "walletRefundAmount");
					if (StringUtils.isNotBlank(walletRefundAmountStr)) {
						// 有钱包金额退款，归还到钱包
						walletRefundAmount = new BigDecimal(walletRefundAmountStr);
					}
					RechargeOrder rechargeOrder = rechargeOrderService.selectByOrderCode(orderCode);
					if (rechargeOrder != null && walletRefundAmount.compareTo(BigDecimal.ZERO) == 0) {
						log.info("是充值的订单但是又没有退还金额给钱包，该次退款是充值订单退款，由充值订单退款逻辑处理");
						return;
					}
					
//					Boolean refundAll = MapUtils.getBoolean(params, "refundAll");// 如果退款需扣手续费，则需按业务的规则计算是否已全额退款
					// TODO 可能需要扣除一些手续费
					Order4Resp order4Resp = orderFeign.selectByOrderCode(orderCode).dataOrThrow();
					BigDecimal needPayAmount = order4Resp.getNeedPayAmount();
					
//					BigDecimal totalRefundAmount = new BigDecimal(MapUtils.getString(params, "totalRefundAmount"));
					BigDecimal totalRefundAmount = order4Resp.getRefundAmount().add(walletRefundAmount);
					BigDecimal serviceAmount = memberBuyOrder.getRefundServiceAmount();
					BigDecimal canRefundAmount = needPayAmount.subtract(serviceAmount).subtract(totalRefundAmount);
					Boolean refundAll = canRefundAmount.compareTo(BigDecimal.ZERO) <= 0;
					
					// 修改‘订单中心’数据
					OrderRefundFinishReq orderRefundFinishReq = new OrderRefundFinishReq();
					orderRefundFinishReq.setOrderCode(orderCode);
					orderRefundFinishReq.setRefundFinishTime(LocalDateTime.now());
//					BigDecimal totalRefundAmount = new BigDecimal(MapUtils.getString(params, "totalRefundAmount"));
					orderRefundFinishReq.setTotalRefundAmount(totalRefundAmount);
					orderRefundFinishReq.setRefundAll(refundAll);
					Boolean updateSuccess = orderFeign.refundFinish(orderRefundFinishReq).dataOrThrow();
					if (!updateSuccess) {
						log.warn("修改‘订单中心’数据失败");
						return;
					}
					
					// 有钱包金额退款，归还到钱包
					if (walletRefundAmount.compareTo(BigDecimal.ZERO) > 0) {
						String uniqueCode = MapUtils.getString(params, "refundOrderCode");
						
						// 退还bu_wallet_use_seq
						BigDecimal returnTotalAmount = walletRefundAmount;
						Map<WalletEnum.Type, BigDecimal> typeAmountMap = walletUseSeqService.calcAndReturn(
								memberBuyOrder.getUserId(),
								Lists.newArrayList(WalletEnum.Type.TO_CHARGE, WalletEnum.Type.TO_GIFT),
								returnTotalAmount);
						
						BigDecimal chargeAmount = typeAmountMap.get(WalletEnum.Type.TO_CHARGE);
//						BigDecimal giftAmount = typeAmountMap.get(WalletEnum.Type.TO_GIFT);
						
						// 优先归还赠送余额
						BigDecimal needReturnGiftAmount = returnTotalAmount.subtract(chargeAmount);
						BigDecimal needReturnChargeAmount = returnTotalAmount.subtract(needReturnGiftAmount);
						
						Integer userId = memberBuyOrder.getUserId();

						MainChargeGiftWalletId walletId = new MainChargeGiftWalletId(userId, WalletEnum.Type.TO_MAIN,
								WalletEnum.Type.TO_CHARGE, WalletEnum.Type.TO_GIFT);

						MainChargeGiftAmount amount = new MainChargeGiftAmount(returnTotalAmount, needReturnChargeAmount, needReturnGiftAmount);
	
						Map<String, Object> attachMap = Maps.newHashMap();
						attachMap.put("businessType", "use_refund");
						attachMap.put("businessId", memberBuyOrder.getId());
						
						freezeMainChargeGiftWallet.income(uniqueCode, walletId, amount, attachMap);
					}
					
					if (refundAll) {
						// 全额退款，才归还优惠券
						Integer userCouponId = memberBuyOrder.getUserCouponId();
						if (userCouponId != null && userCouponId > 0) {// 有选用优惠券，释放优惠券
							userCouponService.updateStatus(userCouponId, "used", "nouse");
						}
					}
				} else {// 退款失败
					// 修改‘订单中心’数据（还原订单状态）
					OrderRefundFailReq orderRefundFailReq = new OrderRefundFailReq();
					orderRefundFailReq.setOrderCode(orderCode);

					String attach = MapUtils.getString(params, "attach");
					Integer oldSubStatusInt = Integer.valueOf(Utils.getByJson(attach, "oldSubStatus"));
					OrderEnum.SubStatusEnum oldSubStatus = OrderEnum.SubStatusEnum.of(oldSubStatusInt);
					orderRefundFailReq.setOldSubStatus(oldSubStatus);
					String message = MapUtils.getString(params, "message");
					orderRefundFailReq.setFailReason(message);

					Boolean updateSuccess = orderFeign.refundFail(orderRefundFailReq).dataOrThrow();
					if (!updateSuccess) {
						log.warn("修改‘订单中心’数据失败");
					}
				}
			}
		});
	}

	@RabbitListener(bindings = @QueueBinding(value = @Queue(value = FanoutConstants.REFUND_APPLY_RESULT.GOODS_REFUND_QUEUE, durable = "false", autoDelete = "true"), exchange = @Exchange(value = FanoutConstants.REFUND_APPLY_RESULT.EXCHANGE, type = ExchangeTypes.FANOUT, durable = "false", autoDelete = "true")))
	public void goodsRefund(String jsonStrMsg, Channel channel, Message message) {
		ConsumerUtils.handleByConsumer(jsonStrMsg, channel, message, new Consumer<Map<String, Object>>() {
			@Override
			public void accept(Map<String, Object> params) {
				log.info("goodsRefund params:{}", JsonUtil.toJsonString(params));

				String orderCode = MapUtils.getString(params, "orderCode");
				// 根据orderCode查询商品订单，查不到的情况下直接退出
				if (!false) {
					log.info("不是商品订单");
					return;
				}
				
				// 处理商品订单逻辑
				Boolean success = MapUtils.getBoolean(params, "success");
				String message = MapUtils.getString(params, "message");
				String refundOrderCode = MapUtils.getString(params, "refundOrderCode");
				String attach = MapUtils.getString(params, "attach");
			}
		});
	}

	@RabbitListener(bindings = @QueueBinding(value = @Queue(value = FanoutConstants.REFUND_APPLY_RESULT.RECHARGE_REFUND_QUEUE, durable = "false", autoDelete = "true"), exchange = @Exchange(value = FanoutConstants.REFUND_APPLY_RESULT.EXCHANGE, type = ExchangeTypes.FANOUT, durable = "false", autoDelete = "true")))
	public void recharge(String jsonStrMsg, Channel channel, Message message) {
		ConsumerUtils.handleByConsumer(jsonStrMsg, channel, message, new Consumer<Map<String, Object>>() {
			@Override
			public void accept(Map<String, Object> params) {
				String orderCode = MapUtils.getString(params, "orderCode");
				// 根据orderCode查询会员订单，查不到的情况下直接退出
				RechargeOrder rechargeOrder = rechargeOrderService.selectByOrderCode(orderCode);
				if (rechargeOrder == null) {
					log.info("不是充值订单");
					return;
				}

				// 处理订单逻辑
				Boolean success = MapUtils.getBoolean(params, "success");
				if (success) {// 退款成功
					Boolean refundAll = MapUtils.getBoolean(params, "refundAll");
					
					// 修改‘订单中心’数据
					OrderRefundFinishReq orderRefundFinishReq = new OrderRefundFinishReq();
					orderRefundFinishReq.setOrderCode(orderCode);
					orderRefundFinishReq.setRefundFinishTime(LocalDateTime.now());
					BigDecimal totalRefundAmount = new BigDecimal(MapUtils.getString(params, "totalRefundAmount"));
					orderRefundFinishReq.setTotalRefundAmount(totalRefundAmount);
					orderRefundFinishReq.setRefundAll(refundAll);
					Boolean updateSuccess = orderFeign.refundFinish(orderRefundFinishReq).dataOrThrow();
					if (!updateSuccess) {
						log.warn("修改‘订单中心’数据失败");
						return;
					}

					if (refundAll) {// 全额退款
						// 扣回余额（注：直接扣回可能存在余额不足导致失败的情况，这种情况下余额有多少扣多少）
						MainChargeGiftWalletId walletId = new MainChargeGiftWalletId(rechargeOrder.getUserId(), Type.TO_MAIN, Type.TO_CHARGE, Type.TO_GIFT);

						MainChargeGiftBalance balance = mainChargeGiftWallet.balance(walletId);
						BigDecimal mainBalance = balance.getMainBalance();
						BigDecimal chargeBalance = balance.getChargeBalance();
						BigDecimal giftBalance = balance.getGiftBalance();

						BigDecimal mainAmount = rechargeOrder.getAmount().add(rechargeOrder.getGiftAmount());
						if (mainAmount.compareTo(mainBalance) > 0) {
							log.warn("{}充值订单退款扣减余额不足,balance:{},amount:{}", Type.TO_MAIN, mainBalance, mainAmount);
							mainAmount = mainBalance;
						}
						BigDecimal chargeAmount = rechargeOrder.getAmount();
						if (chargeAmount.compareTo(chargeBalance) > 0) {
							log.warn("{}充值订单退款扣减余额不足,balance:{},amount:{}", Type.TO_CHARGE, chargeBalance, chargeAmount);
							chargeAmount = chargeBalance;
						}
						BigDecimal giftAmount = rechargeOrder.getGiftAmount();
						if (giftAmount.compareTo(giftBalance) > 0) {
							log.warn("{}充值订单退款扣减余额不足,balance:{},amount:{}", Type.TO_GIFT, giftBalance, giftAmount);
							giftAmount = giftBalance;
						}

						MainChargeGiftAmount amount = new MainChargeGiftAmount(mainAmount, chargeAmount, giftAmount);

						String uniqueCode = MapUtils.getString(params, "refundOrderCode");
						
						Map<String, Object> attachMap = Maps.newHashMap();
						attachMap.put("businessType", "recharge_refund");
						attachMap.put("businessId", rechargeOrder.getId());

						// 冻结的余额无法处理，所以这里只需要用MainChargeGiftWallet的逻辑就行了
						Integer walletRecordId = mainChargeGiftWallet.outcome(uniqueCode, walletId, amount, attachMap);
						if (walletRecordId <= 0) {
							log.warn("{}充值订单退款扣减余额失败,balance:{},amount:{}", JsonUtil.toJsonString(walletId),
									JsonUtil.toJsonString(balance), JsonUtil.toJsonString(amount));
						}
						
						// 扣回bu_wallet_use_seq
						walletUseSeqService.calcAndUse(rechargeOrder.getUserId(),
								Lists.newArrayList(WalletEnum.Type.TO_CHARGE, WalletEnum.Type.TO_GIFT),
								rechargeOrder.getAmount().add(rechargeOrder.getGiftAmount()));
						
						// TODO 根据业务需求，充值订单全退的情况下，决定是否要退回业务订单
						MemberBuyOrder memberBuyOrder = memberBuyOrderService.selectByOrderCode(orderCode);
						if (memberBuyOrder != null) {
							Order4Resp order4Resp = orderFeign.selectByOrderCode(orderCode).dataOrThrow();
							// TODO 这里可以加订单状态、时间等逻辑来判断业务订单是否要退 
							BigDecimal needPayAmount = order4Resp.getNeedPayAmount();
							BigDecimal refundAmount = order4Resp.getRefundAmount();
							BigDecimal leftAmount = needPayAmount.subtract(refundAmount);
							if (leftAmount.compareTo(BigDecimal.ZERO) > 0) {
								String refundOrderCode = String.valueOf(sequenceGenerator.nextId());
								PayRefundApplyReq payRefundApplyReq = new PayRefundApplyReq();
								payRefundApplyReq.setOrderCode(refundOrderCode);
								payRefundApplyReq.setOldOrderCode(orderCode);

								payRefundApplyReq.setAmount(BigDecimal.ZERO);
								payRefundApplyReq.setBusinessType(PayRefundApplyEnum.BusinessType.SYS_AUTO);
								payRefundApplyReq.setVerifyStatus(PayRefundApplyEnum.VerifyStatus.PASS);
								payRefundApplyReq.setReason("充值订单全额退款");

								String attach = Utils.append2Json(null, "oldSubStatus",
										String.valueOf(order4Resp.getSubStatus()));
								attach = Utils.append2Json(attach, "attach",
										Utils.append2Json("{}", "walletRefundAmount", leftAmount.toPlainString()));
								payRefundApplyReq.setAttach(attach);

								refundApplyFeign.refundApply(payRefundApplyReq);
							}
						}
					}
				} else {// 退款失败
					// 修改‘订单中心’数据（还原订单状态）
					OrderRefundFailReq orderRefundFailReq = new OrderRefundFailReq();
					orderRefundFailReq.setOrderCode(orderCode);

					String attach = MapUtils.getString(params, "attach");
					Integer oldSubStatusInt = Integer.valueOf(Utils.getByJson(attach, "oldSubStatus"));
					OrderEnum.SubStatusEnum oldSubStatus = OrderEnum.SubStatusEnum.of(oldSubStatusInt);
					orderRefundFailReq.setOldSubStatus(oldSubStatus);
					String message = MapUtils.getString(params, "message");
					orderRefundFailReq.setFailReason(message);

					Boolean updateSuccess = orderFeign.refundFail(orderRefundFailReq).dataOrThrow();
					if (!updateSuccess) {
						log.warn("修改‘订单中心’数据失败");
					}
				}
			}
		});
	}
}