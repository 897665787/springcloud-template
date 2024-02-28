package com.company.user.controller;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

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
import com.company.order.api.request.PayNotifyReq;
import com.company.order.api.request.PayReq;
import com.company.order.api.request.RegisterOrderReq;
import com.company.order.api.response.OrderResp;
import com.company.order.api.response.PayResp;
import com.company.user.api.constant.Constants;
import com.company.user.api.feign.DistributeOrderFeign;
import com.company.user.api.request.DistributeBuyOrderReq;
import com.company.user.api.request.DistributeBuyOrderReq.UserRemarkReq;
import com.company.user.api.response.DistributeBuyOrderResp;
import com.company.user.api.response.DistributeSubOrderDetailResp;
import com.company.user.api.response.DistributeSubOrderResp;
import com.company.user.coupon.UseCouponService;
import com.company.user.coupon.dto.UserCouponCanUse;
import com.company.user.dto.DistributeAttach;
import com.company.user.service.market.UserCouponService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

/**
 * 配送子订单demo
 */
@Slf4j
@RestController
@RequestMapping("/distributeOrder")
public class DistributeOrderController implements DistributeOrderFeign {
	
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


	@Data
	@Accessors(chain = true)
	public static class ShopCartData {
		String cartType;
		Integer number;
		BigDecimal originAmount;
		BigDecimal salesAmount;
		
		String uniqueCode;
		String productCode;
		String productName;
		String productImage;
		String specJson;
		String specContent;
	}

	private List<ShopCartData> testDataList = Lists.newArrayList(
//			new ShopCartData().setProductCode("M_7").setProductAmount(new BigDecimal("10")).setNumber(7),
//			new ShopCartData().setProductCode("M_30").setProductAmount(new BigDecimal("30")).setNumber(30),
//			new ShopCartData().setProductCode("M_365").setProductAmount(new BigDecimal("300")).setNumber(365)
			);
	
	/**
	 * 购买
	 * 
	 * @param distributeBuyOrderReq
	 * @return
	 */
	@Override
	public Result<DistributeBuyOrderResp> buy(@RequestBody DistributeBuyOrderReq distributeBuyOrderReq) {
		Integer userId = HttpContextUtil.currentUserIdInt();
		// 参数校验
		
		// TODO 从购物车获取商品数据
		if (testDataList.isEmpty()) {
			return Result.fail("购物车未找到商品");
		}
		
		BigDecimal productAmount = BigDecimal.ZERO;
		for (ShopCartData shopCartData : testDataList) {
			BigDecimal salesAmount = shopCartData.getSalesAmount();
			Integer number = shopCartData.getNumber();
			productAmount = productAmount.add(salesAmount.multiply(new BigDecimal(number)));
		}

		// TODO 计算配送费
		BigDecimal distributeAmount = new BigDecimal("2");
		
		// TODO 计算保温费
		BigDecimal baowenAmount = new BigDecimal("1");
		
		// 订单总金额
		BigDecimal orderAmount = productAmount.add(distributeAmount).add(baowenAmount);

		BigDecimal reduceAmount = BigDecimal.ZERO;
		Integer userCouponId = distributeBuyOrderReq.getUserCouponId();
		if (userCouponId != null && userCouponId > 0) {// 有选用优惠券
			Map<String, String> runtimeAttach = Maps.newHashMap();
			String productCodes = testDataList.stream().map(ShopCartData::getProductCode)
					.collect(Collectors.joining(","));
			runtimeAttach.put("productCode", productCodes);
			runtimeAttach.put("orderAmount", orderAmount.toPlainString());
			UserCouponCanUse userCouponCanUse = useCouponService.canUse(userCouponId, userId, orderAmount,
					runtimeAttach);
			if (!userCouponCanUse.getCanUse()) {
				return Result.fail("优惠券不可用");
			}
			reduceAmount = userCouponCanUse.getReduceAmount();
		}

		BigDecimal needPayAmount = orderAmount.subtract(reduceAmount);
		
		BigDecimal payAmount = distributeBuyOrderReq.getPayAmount();
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
		// TODO 创建业务订单（订单中心子订单）
		// userId、orderCode、userCouponId、门店
		
		// 注册到‘订单中心’
		RegisterOrderReq registerOrderReq = new RegisterOrderReq();
		registerOrderReq.setUserId(userId);
		registerOrderReq.setOrderCode(orderCode);
		registerOrderReq.setOrderTypeEnum(OrderEnum.OrderType.DISTRIBUTE);
		registerOrderReq.setSubStatusEnum(OrderEnum.SubStatusEnum.WAIT_PAY);
		registerOrderReq.setProductAmount(productAmount);
		registerOrderReq.setOrderAmount(orderAmount);
		registerOrderReq.setReduceAmount(reduceAmount);
		registerOrderReq.setNeedPayAmount(needPayAmount);
		registerOrderReq.setSubOrderUrl("http://" + Constants.FEIGNCLIENT_VALUE + "/distribute/subOrder");

		List<RegisterOrderReq.OrderProductReq> orderProductReqList = Lists.newArrayList();

		List<UserRemarkReq> userRemarkList = distributeBuyOrderReq.getUserRemarkList();
		Map<String, String> productCodeUserRemarkMap = userRemarkList.stream()
				.collect(Collectors.toMap(UserRemarkReq::getProductCode, UserRemarkReq::getUserRemark, (a, b) -> b));
		for (ShopCartData shopCartData : testDataList) {
			String productCode = shopCartData.getProductCode();
			RegisterOrderReq.OrderProductReq orderProductReq = new RegisterOrderReq.OrderProductReq();
			orderProductReq.setNumber(shopCartData.getNumber());
			orderProductReq.setOriginAmount(shopCartData.getOriginAmount());
			orderProductReq.setSalesAmount(shopCartData.getSalesAmount());
			orderProductReq.setProductCode(productCode);
			orderProductReq.setProductName(shopCartData.getProductName());
			orderProductReq.setProductImage(shopCartData.getProductImage());

			DistributeAttach distributeAttach = new DistributeAttach().setSpecContent(shopCartData.getSpecContent())
					.setUserRemark(productCodeUserRemarkMap.get(productCode));
			orderProductReq.setAttach(JsonUtil.toJsonString(distributeAttach));
			orderProductReqList.add(orderProductReq);
		}

		registerOrderReq.setProductList(orderProductReqList);

		OrderResp orderResp = orderFeign.registerOrder(registerOrderReq).dataOrThrow();
		log.info("orderResp:{}", JsonUtil.toJsonString(orderResp));

		if (needPayAmount.compareTo(BigDecimal.ZERO) == 0) {
			executor.submit(() -> {
				PayNotifyReq payNotifyReq = new PayNotifyReq();
				payNotifyReq.setEvent(PayNotifyReq.EVENT.PAY);
				payNotifyReq.setSuccess(true);
				payNotifyReq.setMessage("0元付，跳过支付流程");
				payNotifyReq.setOrderCode(orderCode);
				payNotifyReq.setTime(LocalDateTime.now());
				Result<Void> buyNotifyResult = buyNotify(payNotifyReq);
				log.info("buyNotify:{}", JsonUtil.toJsonString(buyNotifyResult));
			});
			return Result.success(new DistributeBuyOrderResp().setNeedPay(false));
		}

		// 获取支付参数
		PayReq payReq = new PayReq();
		payReq.setUserId(userId);
		payReq.setOrderCode(orderCode);
		payReq.setBusinessType(OrderPayEnum.BusinessType.DISTRIBUTE);
		payReq.setMethod(OrderPayEnum.Method.of(distributeBuyOrderReq.getPayMethod()));
		payReq.setAppid("wxeb6ffb3sdadda333");
		payReq.setAmount(needPayAmount);
		payReq.setBody("购买会员");
		payReq.setSpbillCreateIp(HttpContextUtil.requestip());
		payReq.setOpenid(HttpContextUtil.deviceid());
		payReq.setNotifyUrl("http://" + Constants.FEIGNCLIENT_VALUE + "/distribute/buyNotify");
		PayResp payResp = payFeign.unifiedorder(payReq).dataOrThrow();
		if (!payResp.getSuccess()) {
			return Result.fail("支付失败，请稍后重试");
		}
		return Result.success(new DistributeBuyOrderResp().setNeedPay(false).setPayInfo(payResp.getPayInfo()));
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
			orderFeign.cancel(new OrderCancelReq().setOrderCode(orderCode).setCancelTime(time));

			Integer userCouponId = 0;// TODO 根据业务订单获得
			if (userCouponId != null && userCouponId > 0) {// 有选用优惠券，锁定优惠券
				userCouponService.updateStatus(userCouponId, "used", "nouse");
			}
			
			return Result.success();
		}
		
		if (!payNotifyReq.getSuccess()) {// 支付失败
			// 发布‘支付失败’事件
			Map<String, Object> params = Maps.newHashMap();
			params.put("orderCode", orderCode);
			messageSender.sendFanoutMessage(params, FanoutConstants.DISTRIBUTE_PAY_FAIL.EXCHANGE);
			
			return Result.success();
		}
		// 支付成功

		// 修改‘订单中心’数据
		orderFeign.paySuccess(new OrderPaySuccessReq().setOrderCode(orderCode).setPayTime(time));
		
    	// 发布‘支付成功’事件
		Map<String, Object> params = Maps.newHashMap();
		params.put("orderCode", orderCode);
		messageSender.sendFanoutMessage(params, FanoutConstants.DISTRIBUTE_PAY_SUCCESS.EXCHANGE);
    	
		// TODO 会员过期时间续期，根据业务订单获得‘续期时间长’
		
		// 修改‘订单中心’数据
		orderFeign.finish(new OrderFinishReq().setOrderCode(orderCode).setFinishTime(time));
		
		return Result.success();
	}

	/**
	 * 根据订单号查询子订单详情(使用restTemplate的方式调用)
	 * 
	 * @param orderReq
	 * @return
	 */
	@PostMapping("/subOrder")
	public Result<Object> subOrder(@RequestBody OrderReq orderReq) {
		OrderEnum.SearchTypeEnum searchType = orderReq.getSearchType();
		if (searchType == OrderEnum.SearchTypeEnum.ITEM) {
			return Result.success(item(orderReq));
		} else if (searchType == OrderEnum.SearchTypeEnum.DETAIL) {
			return Result.success(detail(orderReq));
		}
		return Result.success();
	}

	private DistributeSubOrderResp item(OrderReq orderReq) {
		DistributeSubOrderResp resp = new DistributeSubOrderResp();
		resp.setMealCode("123456");
		return resp;
	}
	
	private DistributeSubOrderDetailResp detail(OrderReq orderReq) {
		DistributeSubOrderDetailResp resp = new DistributeSubOrderDetailResp();
		resp.setMealCode("123456");

		List<OrderReq.ProductReq> productList = orderReq.getProductList();
		List<DistributeSubOrderDetailResp.ProductDetailResp> productDetailRespList = productList.stream().map(v -> {
			DistributeSubOrderDetailResp.ProductDetailResp productDetailResp = new DistributeSubOrderDetailResp.ProductDetailResp();
			productDetailResp.setNumber(v.getNumber());
			productDetailResp.setOriginAmount(v.getOriginAmount());
			productDetailResp.setSalesAmount(v.getSalesAmount());
			productDetailResp.setAmount(v.getAmount());
			productDetailResp.setProductCode(v.getProductCode());
			productDetailResp.setProductName(v.getProductName());
			productDetailResp.setProductImage(v.getProductImage());
			
			String attach = v.getAttach();
			if(StringUtils.isNotBlank(attach)){
				DistributeAttach distributeAttach = JsonUtil.toEntity(attach, DistributeAttach.class);
				productDetailResp.setSpecContent(distributeAttach.getSpecContent());
				productDetailResp.setUserRemark(distributeAttach.getUserRemark());
			}
			return productDetailResp;
		}).collect(Collectors.toList());
		resp.setProductList(productDetailRespList);
		
		List<DistributeSubOrderDetailResp.TextValueResp> textValueList = Lists.newArrayList();
		textValueList.add(new DistributeSubOrderDetailResp.TextValueResp().setText("aaaaa").setValue("bbbbb"));
		resp.setTextValueList(textValueList);
		return resp;
	}
}
