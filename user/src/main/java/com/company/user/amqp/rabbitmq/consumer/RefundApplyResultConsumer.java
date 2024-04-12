package com.company.user.amqp.rabbitmq.consumer;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.function.Consumer;

import org.apache.commons.collections4.MapUtils;
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
import com.company.order.api.enums.OrderEnum;
import com.company.order.api.feign.OrderFeign;
import com.company.order.api.request.OrderRefundFinishReq;
import com.company.order.api.request.OrderRefundRejectReq;
import com.company.user.entity.MemberBuyOrder;
import com.company.user.service.MemberBuyOrderService;
import com.company.user.service.market.UserCouponService;
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
//					Boolean refundAll = MapUtils.getBoolean(params, "refundAll");// 如果退款需扣手续费，则需按业务的规则计算是否已全额退款
					// TODO 可能需要扣除一些手续费
					BigDecimal payAmount = new BigDecimal(MapUtils.getString(params, "payAmount"));
					BigDecimal totalRefundAmount = new BigDecimal(MapUtils.getString(params, "totalRefundAmount"));
//					BigDecimal serviceAmount = payAmount.multiply(new BigDecimal("0.1"));// 扣除10%作为服务费
					BigDecimal serviceAmount = memberBuyOrder.getRefundServiceAmount();
					BigDecimal canRefundAmount = payAmount.subtract(serviceAmount).subtract(totalRefundAmount);
					Boolean refundAll = canRefundAmount.compareTo(BigDecimal.ZERO) == 0;
					
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
					OrderRefundRejectReq orderRefundRejectReq = new OrderRefundRejectReq();
					orderRefundRejectReq.setOrderCode(orderCode);

					String attach = MapUtils.getString(params, "attach");
					Integer oldSubStatusInt = Integer.valueOf(Utils.getByJson(attach, "oldSubStatus"));
					OrderEnum.SubStatusEnum oldSubStatus = OrderEnum.SubStatusEnum.of(oldSubStatusInt);
					orderRefundRejectReq.setOldSubStatus(oldSubStatus);
					String message = MapUtils.getString(params, "message");
					orderRefundRejectReq.setRejectReason(message);

					Boolean updateSuccess = orderFeign.refundReject(orderRefundRejectReq).dataOrThrow();
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
}