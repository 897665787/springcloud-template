package com.company.user.controller;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.company.common.api.Result;
import com.company.common.util.JsonUtil;
import com.company.framework.amqp.MessageSender;
import com.company.framework.amqp.rabbit.constants.FanoutConstants;
import com.company.framework.context.HttpContextUtil;
import com.company.framework.sequence.SequenceGenerator;
import com.company.order.api.enums.OrderEnum;
import com.company.order.api.enums.OrderPayEnum;
import com.company.order.api.feign.OrderFeign;
import com.company.order.api.feign.PayFeign;
import com.company.order.api.request.OrderCancelReq;
import com.company.order.api.request.OrderFinishReq;
import com.company.order.api.request.OrderPaySuccessReq;
import com.company.order.api.request.OrderReq;
import com.company.order.api.request.PayCloseReq;
import com.company.order.api.request.PayNotifyReq;
import com.company.order.api.request.PayReq;
import com.company.order.api.request.RegisterOrderReq;
import com.company.order.api.response.OrderResp;
import com.company.order.api.response.PayResp;
import com.company.user.api.constant.Constants;
import com.company.user.api.feign.MemberBuyFeign;
import com.company.user.api.request.MemberBuyOrderReq;
import com.company.user.api.response.MemberBuyOrderResp;
import com.company.user.api.response.MemberBuySubOrderDetailResp;
import com.company.user.api.response.MemberBuySubOrderResp;
import com.company.user.coupon.UseCouponService;
import com.company.user.coupon.dto.UserCouponCanUse;
import com.company.user.dto.MemberBuyAttach;
import com.company.user.entity.MemberBuyOrder;
import com.company.user.service.MemberBuyOrderService;
import com.company.user.service.MemberService;
import com.company.user.service.MemberService.MemberData;
import com.company.user.service.market.UserCouponService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import lombok.extern.slf4j.Slf4j;

/**
 * 购买会员子订单demo
 */
@Slf4j
@RestController
@RequestMapping("/memberBuy")
public class MemberBuyController implements MemberBuyFeign {
	
	@Autowired
	private SequenceGenerator sequenceGenerator;

	@Autowired
	private OrderFeign orderFeign;

	@Autowired
	private PayFeign payFeign;
	
	@Autowired
	private MessageSender messageSender;
	
	@Autowired
	private UseCouponService useCouponService;
	
	@Autowired
	private UserCouponService userCouponService;
	
	@Autowired
	private ThreadPoolTaskExecutor executor;
	
	@Autowired
	private MemberService memberService;
	@Autowired
	private MemberBuyOrderService memberBuyOrderService;
	
	/**
	 * 购买
	 * 
	 * @param memberBuyOrderReq
	 * @return
	 */
	@Override
	public Result<MemberBuyOrderResp> buy(@RequestBody MemberBuyOrderReq memberBuyOrderReq) {
		Integer userId = HttpContextUtil.currentUserIdInt();
		// 参数校验
		Integer number = memberBuyOrderReq.getNumber();
		
		String productCode = memberBuyOrderReq.getProductCode();
		MemberData memberData = memberService.selectByProductCode(productCode);
		if (memberData == null) {
			return Result.fail("商品不存在");
		}
		// TODO 通过productCode得到
		BigDecimal productAmount = memberData.getProductAmount();

		// 订单总金额
		BigDecimal orderAmount = productAmount.multiply(new BigDecimal(number));

		BigDecimal reduceAmount = BigDecimal.ZERO;
		Integer userCouponId = memberBuyOrderReq.getUserCouponId();
		if (userCouponId != null && userCouponId > 0) {// 有选用优惠券
			Map<String, String> runtimeAttach = Maps.newHashMap();
			runtimeAttach.put("productCode", productCode);
			runtimeAttach.put("orderAmount", orderAmount.toPlainString());
			UserCouponCanUse userCouponCanUse = useCouponService.canUse(userCouponId, userId, orderAmount,
					runtimeAttach);
			if (!userCouponCanUse.getCanUse()) {
				return Result.fail("优惠券不可用");
			}
			reduceAmount = userCouponCanUse.getReduceAmount();
		}

		BigDecimal needPayAmount = orderAmount.subtract(reduceAmount);
		
		BigDecimal payAmount = memberBuyOrderReq.getPayAmount();
		if (payAmount.compareTo(needPayAmount) != 0) {
			return Result.fail("支付金额不匹配");
		}

		if (userCouponId != null && userCouponId > 0) {// 有选用优惠券，锁定优惠券
			Integer affect = userCouponService.updateStatus(userCouponId, "nouse", "used");
			if (affect == 0) {
				return Result.fail("优惠券不可用");
			}
		}
		
		// TODO 条件校验（下单限制、风控）

		String orderCode = String.valueOf(sequenceGenerator.nextId());
		
		// 创建业务订单（订单中心子订单）
		MemberBuyOrder memberBuyOrder = new MemberBuyOrder();
		memberBuyOrder.setUserId(userId);
		memberBuyOrder.setOrderCode(orderCode);
		memberBuyOrder.setUserCouponId(userCouponId);
		memberBuyOrder.setProductCode(memberData.getProductCode());
		memberBuyOrder.setAmount(memberData.getProductAmount());
		memberBuyOrder.setAddDays(memberData.getAddDays());
		memberBuyOrderService.insert(memberBuyOrder);
		
		// 注册到‘订单中心’
		RegisterOrderReq registerOrderReq = new RegisterOrderReq();
		registerOrderReq.setUserId(userId);
		registerOrderReq.setOrderCode(orderCode);
		registerOrderReq.setOrderTypeEnum(OrderEnum.OrderType.BUY_MEMBER);
		registerOrderReq.setSubStatusEnum(OrderEnum.SubStatusEnum.WAIT_PAY);
		registerOrderReq.setProductAmount(productAmount);
		registerOrderReq.setOrderAmount(orderAmount);
		registerOrderReq.setReduceAmount(reduceAmount);
		registerOrderReq.setNeedPayAmount(needPayAmount);
		registerOrderReq.setSubOrderUrl("http://" + Constants.FEIGNCLIENT_VALUE + "/memberBuy/subOrder");

		MemberBuyAttach memberBuyAttach = new MemberBuyAttach().setUserRemark(memberBuyOrderReq.getUserRemark());
		registerOrderReq.setAttach(JsonUtil.toJsonString(memberBuyAttach));

		List<RegisterOrderReq.OrderProductReq> orderProductReqList = Lists.newArrayList();

		RegisterOrderReq.OrderProductReq orderProductReq = new RegisterOrderReq.OrderProductReq();
		orderProductReq.setNumber(number);
		orderProductReq.setOriginAmount(productAmount);
		orderProductReq.setSalesAmount(productAmount);
		orderProductReq.setProductCode(productCode);
		// TODO 通过productCode得到
		String productName = "会员月卡";
		String productImage = "http://www.image.com/member_month.png";
		orderProductReq.setProductName(productName);
		orderProductReq.setProductImage(productImage);
		orderProductReqList.add(orderProductReq);

		registerOrderReq.setProductList(orderProductReqList);

		OrderResp orderResp = orderFeign.registerOrder(registerOrderReq).dataOrThrow();
		log.info("orderResp:{}", JsonUtil.toJsonString(orderResp));

		if (needPayAmount.compareTo(BigDecimal.ZERO) == 0) {
			executor.submit(() -> {
				PayNotifyReq payNotifyReq = new PayNotifyReq();
				payNotifyReq.setEvent(PayNotifyReq.EVENT.PAY);
				payNotifyReq.setOrderCode(orderCode);
				payNotifyReq.setPayAmount(needPayAmount);
				payNotifyReq.setTime(LocalDateTime.now());
				Result<Void> buyNotifyResult = buyNotify(payNotifyReq);
				log.info("buyNotify:{}", JsonUtil.toJsonString(buyNotifyResult));
			});
			return Result.success(new MemberBuyOrderResp().setNeedPay(false));
		}

		// 获取支付参数
		PayReq payReq = new PayReq();
		payReq.setUserId(userId);
		payReq.setOrderCode(orderCode);
		payReq.setBusinessType(OrderPayEnum.BusinessType.MEMBER);
		payReq.setMethod(OrderPayEnum.Method.of(memberBuyOrderReq.getPayMethod()));
		payReq.setAppid(memberBuyOrderReq.getAppid());
		payReq.setAmount(needPayAmount);
		payReq.setBody("购买会员");
		payReq.setSpbillCreateIp(HttpContextUtil.requestip());
//		payReq.setProductId(productId);
		payReq.setOpenid(HttpContextUtil.deviceid());
		payReq.setNotifyUrl("http://" + Constants.FEIGNCLIENT_VALUE + "/memberBuy/buyNotify");
//		payReq.setAttach(attach);
//		payReq.setTimeoutSeconds(timeoutSeconds);
//		payReq.setRemark(remark);
		PayResp payResp = payFeign.unifiedorder(payReq).dataOrThrow();
		if (!payResp.getSuccess()) {
			return Result.fail("支付失败，请稍后重试");
		}
		return Result.success(new MemberBuyOrderResp().setNeedPay(true).setPayInfo(payResp.getPayInfo()));
	}

	/**
	 * 购买回调(使用restTemplate的方式调用)
	 * 
	 * @param payNotifyReq
	 * @return
	 */
	@PostMapping("/buyNotify")
	public Result<Void> buyNotify(@RequestBody PayNotifyReq payNotifyReq) {
		String orderCode = payNotifyReq.getOrderCode();
		LocalDateTime time = payNotifyReq.getTime();
		
		if (Objects.equals(payNotifyReq.getEvent(), PayNotifyReq.EVENT.CLOSE)) { // 超时未支付关闭订单回调
			log.info("超时未支付关闭订单回调");
			// 修改‘订单中心’数据
			OrderCancelReq orderCancelReq = new OrderCancelReq().setOrderCode(orderCode).setCancelTime(time);
			Boolean cancelByTimeout = orderFeign.cancelByTimeout(orderCancelReq).dataOrThrow();
			if (!cancelByTimeout) {
				log.warn("cancelByTimeout,修改‘订单中心’数据失败:{}", JsonUtil.toJsonString(orderCancelReq));
				return Result.success();
			}

			MemberBuyOrder memberBuyOrder = memberBuyOrderService.selectByOrderCode(orderCode);
			Integer userCouponId = memberBuyOrder.getUserCouponId();
			if (userCouponId != null && userCouponId > 0) {// 有选用优惠券，释放优惠券
				userCouponService.updateStatus(userCouponId, "used", "nouse");
			}
			
			return Result.success();
		}
		
		// 支付成功

		// 可能存在订单已经因超时取消了，但用户又支付了的场景，所以订单可以由‘已关闭’变为‘已支付’，所以关闭订单的逻辑需要反着处理一次
		MemberBuyOrder memberBuyOrder = memberBuyOrderService.selectByOrderCode(orderCode);
		Integer userCouponId = memberBuyOrder.getUserCouponId();
		if (userCouponId != null && userCouponId > 0) {// 有选用优惠券，使用优惠券
			userCouponService.updateStatus(userCouponId, "nouse", "used");
		}
		
		// 修改‘订单中心’数据
		BigDecimal payAmount = payNotifyReq.getPayAmount();
		OrderPaySuccessReq orderPaySuccessReq = new OrderPaySuccessReq().setOrderCode(orderCode).setPayAmount(payAmount)
				.setPayTime(time);
		Boolean updateSuccess = orderFeign.paySuccess(orderPaySuccessReq).dataOrThrow();
		if (!updateSuccess) {
			log.warn("paySuccess,修改‘订单中心’数据失败:{}", JsonUtil.toJsonString(orderPaySuccessReq));
			return Result.success();
		}
		
    	// 发布‘支付成功’事件
		Map<String, Object> params = Maps.newHashMap();
		params.put("orderCode", orderCode);
		messageSender.sendFanoutMessage(params, FanoutConstants.MEMBER_BUY_PAY_SUCCESS.EXCHANGE);
    	
		// TODO 会员过期时间续期，根据业务订单获得‘续期时间长’
//		Integer addDays = memberBuyOrder.getAddDays();
		
		// 修改‘订单中心’数据
		orderFeign.finish(new OrderFinishReq().setOrderCode(orderCode).setFinishTime(time));
		
		return Result.success();
	}

	/**
	 * 根据订单号执行/查询子订单(使用restTemplate的方式调用)
	 * 
	 * @param orderReq
	 * @return
	 */
	@PostMapping("/subOrder")
	public Result<Object> subOrder(@RequestBody OrderReq orderReq) {
		OrderEnum.SubOrderEventEnum subOrderEvent = orderReq.getSubOrderEvent();
		if (subOrderEvent == OrderEnum.SubOrderEventEnum.USER_CANCEL) {
			userCancel(orderReq);
			return Result.success(detail(orderReq));
		} else if (subOrderEvent == OrderEnum.SubOrderEventEnum.QUERY_ITEM) {
			return Result.success(item(orderReq));
		} else if (subOrderEvent == OrderEnum.SubOrderEventEnum.QUERY_DETAIL) {
			return Result.success(detail(orderReq));
		} else if (subOrderEvent == OrderEnum.SubOrderEventEnum.CALC_CANREFUNDAMOUNT) { // 可不处理
			return Result.success(calcCanRefundAmount(orderReq));
		}
		return Result.success();
	}

	private void userCancel(OrderReq orderReq) {
		String orderCode = orderReq.getOrderCode();
		
		MemberBuyOrder memberBuyOrder = memberBuyOrderService.selectByOrderCode(orderCode);
		Integer userCouponId = memberBuyOrder.getUserCouponId();
		if (userCouponId != null && userCouponId > 0) {// 有选用优惠券，释放优惠券
			userCouponService.updateStatus(userCouponId, "used", "nouse");
		}
		
		if (orderReq.getNeedPayAmount().compareTo(BigDecimal.ZERO) > 0) {
			// 关闭支付订单，不关心结果
			PayCloseReq payCloseReq = new PayCloseReq();
			payCloseReq.setOrderCode(orderCode);
			Result<Void> payCloseResp = payFeign.payClose(payCloseReq);
			log.info("关闭订单结果:{}", JsonUtil.toJsonString(payCloseResp));
		}
	}

	private MemberBuySubOrderResp item(OrderReq orderReq) {
		MemberBuySubOrderResp resp = new MemberBuySubOrderResp();
		
		String orderCode = orderReq.getOrderCode();
		MemberBuyOrder memberBuyOrder = memberBuyOrderService.selectByOrderCode(orderCode);
		Integer addDays = memberBuyOrder.getAddDays();
		resp.setAddDays(addDays);
		
		return resp;
	}
	
	private MemberBuySubOrderDetailResp detail(OrderReq orderReq) {
		MemberBuySubOrderDetailResp resp = new MemberBuySubOrderDetailResp();
		
		String orderCode = orderReq.getOrderCode();
		MemberBuyOrder memberBuyOrder = memberBuyOrderService.selectByOrderCode(orderCode);
		Integer addDays = memberBuyOrder.getAddDays();
		resp.setAddDays(addDays);
		
		String attach = orderReq.getAttach();
		if(StringUtils.isNotBlank(attach)){
			MemberBuyAttach distributeAttach = JsonUtil.toEntity(attach, MemberBuyAttach.class);
			resp.setUserRemark(distributeAttach.getUserRemark());
		}
		
		return resp;
	}

	private BigDecimal calcCanRefundAmount(OrderReq orderReq) {
		BigDecimal payAmount = orderReq.getPayAmount();
		BigDecimal refundAmount = orderReq.getRefundAmount();
		// BigDecimal canRefundAmount = payAmount.subtract(refundAmount);// 原逻辑
		// TODO 可能需要扣除一些手续费
		BigDecimal serviceAmount = payAmount.multiply(new BigDecimal("0.1"));// 扣除10%作为服务费，如12306，根据时间来计算手续费
		memberBuyOrderService.updateRefundServiceAmountByOrderCode(serviceAmount, orderReq.getOrderCode());// 这个费用最好记录到子业务
		BigDecimal canRefundAmount = payAmount.subtract(serviceAmount).subtract(refundAmount);
		return canRefundAmount;
	}
}
