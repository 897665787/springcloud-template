package com.company.user.controller;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
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
import com.company.order.api.enums.OrderEnum.StatusEnum;
import com.company.order.api.enums.OrderPayEnum;
import com.company.order.api.feign.OrderFeign;
import com.company.order.api.feign.PayFeign;
import com.company.order.api.request.OrderCancelReq;
import com.company.order.api.request.OrderPaySuccessReq;
import com.company.order.api.request.OrderReq;
import com.company.order.api.request.OrderReq.ProductReq;
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
import com.company.user.service.ShopCartService;
import com.company.user.service.ShopCartService.ShopCart;
import com.company.user.service.ShopProductService;
import com.company.user.service.ShopProductService.ShopProduct;
import com.company.user.service.ShopService;
import com.company.user.service.ShopService.Shop;
import com.company.user.service.market.UserCouponService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

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
	
	@Autowired
	private ShopCartService shopCartService;
	@Autowired
	private ShopService shopService;
	@Autowired
	private ShopProductService shopProductService;
	
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
		List<ShopCart> shopCartList = shopCartService.selectByUserId(userId);
		if (shopCartList.isEmpty()) {
			return Result.fail("购物车未找到商品");
		}
		
		BigDecimal productAmount = BigDecimal.ZERO;
		for (ShopCart shopCart : shopCartList) {
			BigDecimal salesAmount = shopCart.getSalesAmount();
			Integer number = shopCart.getNumber();
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
			String productCodes = shopCartList.stream().map(ShopCart::getProductCode).collect(Collectors.joining(","));
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
		registerOrderReq.setSubOrderUrl("http://" + Constants.FEIGNCLIENT_VALUE + "/distributeOrder/subOrder");

		List<RegisterOrderReq.OrderProductReq> orderProductReqList = Lists.newArrayList();

		List<UserRemarkReq> userRemarkList = distributeBuyOrderReq.getUserRemarkList();
		Map<String, String> productCodeUserRemarkMap = userRemarkList.stream()
				.collect(Collectors.toMap(UserRemarkReq::getProductCode, UserRemarkReq::getUserRemark, (a, b) -> b));
		for (ShopCart shopCart : shopCartList) {
			String productCode = shopCart.getProductCode();
			RegisterOrderReq.OrderProductReq orderProductReq = new RegisterOrderReq.OrderProductReq();
			orderProductReq.setNumber(shopCart.getNumber());
			orderProductReq.setOriginAmount(shopCart.getOriginAmount());
			orderProductReq.setSalesAmount(shopCart.getSalesAmount());
			orderProductReq.setProductCode(productCode);
			orderProductReq.setProductName(shopCart.getProductName());
			orderProductReq.setProductImage(shopCart.getProductImage());

			ShopProduct shopProduct = shopProductService.selectByProductCode(productCode);
			Shop shop = shopService.selectByShopCode(shopProduct.getShopCode());
			String shopCode = shop.getShopCode();
			String shopName = shop.getShopName();
			String shopLogo = shop.getShopLogo();
			DistributeAttach distributeAttach = new DistributeAttach().setSpecContent(shopCart.getSpecContent())
					.setUserRemark(productCodeUserRemarkMap.get(productCode)).setShopCode(shopCode)
					.setShopName(shopName).setShopLogo(shopLogo);
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
		payReq.setAppid(distributeBuyOrderReq.getAppid());
		payReq.setAmount(needPayAmount);
		payReq.setBody("配送下单");
		payReq.setSpbillCreateIp(HttpContextUtil.requestip());
		payReq.setOpenid(HttpContextUtil.deviceid());
		payReq.setNotifyUrl("http://" + Constants.FEIGNCLIENT_VALUE + "/distributeOrder/buyNotify");
		PayResp payResp = payFeign.unifiedorder(payReq).dataOrThrow();
		if (!payResp.getSuccess()) {
			return Result.fail("支付失败，请稍后重试");
		}
		return Result.success(new DistributeBuyOrderResp().setNeedPay(true).setPayInfo(payResp.getPayInfo()));
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
		resp.setDistributeAmount(new BigDecimal("2"));
		resp.setBaowenAmount(new BigDecimal("1"));
		
		// 只有发货之后才有取餐码
		StatusEnum statusEnum = orderReq.getStatus();
		if (statusEnum == StatusEnum.WAIT_RECEIVE || statusEnum == StatusEnum.COMPLETE || statusEnum == StatusEnum.REFUND) {
			resp.setMealCode("123456");
		}

		List<OrderReq.ProductReq> productList = orderReq.getProductList();
		Integer totalNumber = productList.stream().map(OrderReq.ProductReq::getNumber).reduce(Integer::sum).orElse(0);
		resp.setTotalNumber(totalNumber);

		ProductReq productReq = productList.get(0);
		resp.setProductCode(productReq.getProductCode());
		resp.setProductName(productReq.getProductName());
		resp.setProductImage(productReq.getProductImage());

		String attach = productReq.getAttach();
		DistributeAttach distributeAttach = JsonUtil.toEntity(attach, DistributeAttach.class);
		resp.setShopCode(distributeAttach.getShopCode());
		resp.setShopName(distributeAttach.getShopName());
		resp.setShopLogo(distributeAttach.getShopLogo());
		
		List<DistributeSubOrderResp.BottonResp> bottonList = Lists.newArrayList();
		if (OrderEnum.StatusEnum.WAIT_RECEIVE == statusEnum || OrderEnum.StatusEnum.COMPLETE == statusEnum
				|| OrderEnum.StatusEnum.REFUND == statusEnum) { // 发货成功之后才有物流信息
			bottonList.add(new DistributeSubOrderResp.BottonResp("查看物流", "/{前端提供页面路径和需要的参数}/delivery?orderCode=" + orderReq.getOrderCode(), 40));
			bottonList.add(new DistributeSubOrderResp.BottonResp("保险服务", "/{前端提供页面路径和需要的参数}/baoxian?orderCode=" + orderReq.getOrderCode(), 60));
		}
		resp.setBottonList(bottonList);
		return resp;
	}

	private DistributeSubOrderDetailResp detail(OrderReq orderReq) {
		DistributeSubOrderDetailResp resp = new DistributeSubOrderDetailResp();
		resp.setDistributeAmount(new BigDecimal("2"));
		resp.setBaowenAmount(new BigDecimal("1"));
		
		// 只有发货之后才有取餐码
		StatusEnum statusEnum = orderReq.getStatus();
		if (statusEnum == StatusEnum.WAIT_RECEIVE || statusEnum == StatusEnum.COMPLETE || statusEnum == StatusEnum.REFUND) {
			resp.setMealCode("123456");
		}

		List<OrderReq.ProductReq> productList = orderReq.getProductList();
		Map<DistributeSubOrderDetailResp.ShopResp, List<OrderReq.ProductReq>> shopCodeProductListMap = productList
				.stream().collect(Collectors.groupingBy(v -> {
					String attach = v.getAttach();
					DistributeAttach distributeAttach = JsonUtil.toEntity(attach, DistributeAttach.class);

					DistributeSubOrderDetailResp.ShopResp shopResp = new DistributeSubOrderDetailResp.ShopResp();
					shopResp.setShopCode(distributeAttach.getShopCode());
					shopResp.setShopName(distributeAttach.getShopName());
					shopResp.setShopLogo(distributeAttach.getShopLogo());
					return shopResp;
				}, LinkedHashMap::new, Collectors.toList()));

		List<DistributeSubOrderDetailResp.ShopResp> shopRespList = Lists.newArrayList();
		Set<Entry<DistributeSubOrderDetailResp.ShopResp, List<ProductReq>>> entrySet = shopCodeProductListMap
				.entrySet();
		for (Entry<DistributeSubOrderDetailResp.ShopResp, List<ProductReq>> entry : entrySet) {
			DistributeSubOrderDetailResp.ShopResp key = entry.getKey();
			List<ProductReq> value = entry.getValue();

			List<DistributeSubOrderDetailResp.ShopResp.ProductDetailResp> productDetailRespList = value.stream()
					.map(v -> {
						DistributeSubOrderDetailResp.ShopResp.ProductDetailResp productDetailResp = new DistributeSubOrderDetailResp.ShopResp.ProductDetailResp();
						productDetailResp.setNumber(v.getNumber());
						productDetailResp.setOriginAmount(v.getOriginAmount());
						productDetailResp.setSalesAmount(v.getSalesAmount());
						productDetailResp.setAmount(v.getAmount());
						productDetailResp.setProductCode(v.getProductCode());
						productDetailResp.setProductName(v.getProductName());
						productDetailResp.setProductImage(v.getProductImage());

						String attach = v.getAttach();
						if (StringUtils.isNotBlank(attach)) {
							DistributeAttach distributeAttach = JsonUtil.toEntity(attach, DistributeAttach.class);
							productDetailResp.setSpecContent(distributeAttach.getSpecContent());
							productDetailResp.setUserRemark(distributeAttach.getUserRemark());
						}
						return productDetailResp;
					}).collect(Collectors.toList());

			DistributeSubOrderDetailResp.ShopResp shopResp = new DistributeSubOrderDetailResp.ShopResp();
			shopResp.setShopCode(key.getShopCode());
			shopResp.setShopName(key.getShopName());
			shopResp.setShopLogo(key.getShopLogo());
			shopResp.setProductList(productDetailRespList);
			shopRespList.add(shopResp);
		}

		resp.setShopList(shopRespList);

		List<DistributeSubOrderDetailResp.TextValueResp> textValueList = Lists.newArrayList();
		textValueList.add(new DistributeSubOrderDetailResp.TextValueResp("补一条数据展示", "bbbbb"));
		resp.setTextValueList(textValueList);
		return resp;
	}
}
