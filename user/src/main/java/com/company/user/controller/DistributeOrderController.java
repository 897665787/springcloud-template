package com.company.user.controller;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.company.order.api.request.OrderPaySuccessReq;
import com.company.order.api.request.OrderReq;
import com.company.order.api.request.OrderReq.ProductReq;
import com.company.order.api.request.PayNotifyReq;
import com.company.order.api.request.PayReq;
import com.company.order.api.request.RegisterOrderReq;
import com.company.order.api.response.PayResp;
import com.company.user.api.constant.Constants;
import com.company.user.api.feign.DistributeOrderFeign;
import com.company.user.api.request.DistributeOrderReq;
import com.company.user.api.response.DistributeSubOrderDetailResp;
import com.company.user.api.response.DistributeSubOrderDetailResp.ProductDetailResp;
import com.company.user.api.response.DistributeSubOrderDetailResp.TextValueResp;
import com.company.user.api.response.DistributeSubOrderResp;
import com.company.user.dto.DistributeAttach;
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

	/**
	 * 购买
	 * 
	 * @param memberBuyOrderReq
	 * @return
	 */
	@Override
	public Result<?> buy(@RequestBody DistributeOrderReq distributeOrderReq) {
		Integer userId = HttpContextUtil.currentUserIdInt();
		// 参数校验
		BigDecimal orderAmount = distributeOrderReq.getOrderAmount();
		BigDecimal payAmount = distributeOrderReq.getPayAmount();
		BigDecimal needPayAmount = new BigDecimal("77.5");
		if (payAmount.compareTo(needPayAmount) != 0) {
			return Result.fail("");
		}
		
		// 条件校验（下单限制、风控）
		

		// 创建业务订单（订单中心子订单）
		String orderCode = String.valueOf(sequenceGenerator.nextId());

		// 注册到‘订单中心’
		BigDecimal productAmount = new BigDecimal("80");
		BigDecimal reduceAmount = new BigDecimal("2.5");
		
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
		
		RegisterOrderReq.OrderProductReq orderProductReq = new RegisterOrderReq.OrderProductReq();
		orderProductReq.setNumber(1);
		orderProductReq.setOriginAmount(new BigDecimal("20"));
		orderProductReq.setSalesAmount(new BigDecimal("20"));
		orderProductReq.setProductCode("1212313");
		orderProductReq.setProductName("卡片");
		orderProductReq.setProductImage("http://www.image.com/aaa.png");
		
		DistributeAttach distributeAttach = new DistributeAttach().setSpecContent("辣/加料");
		orderProductReq.setAttach(JsonUtil.toJsonString(distributeAttach));
		orderProductReqList.add(orderProductReq);
		
		RegisterOrderReq.OrderProductReq orderProductReq2 = new RegisterOrderReq.OrderProductReq();
		orderProductReq2.setNumber(2);
		orderProductReq2.setOriginAmount(new BigDecimal("30"));
		orderProductReq2.setSalesAmount(new BigDecimal("30"));
		orderProductReq2.setProductCode("561616");
		orderProductReq2.setProductName("卡片2");
		orderProductReq2.setProductImage("http://www.image.com/aaa22.png");
		
		DistributeAttach distributeAttach2 = new DistributeAttach().setSpecContent("辣/热").setUserRemark("汤饭分离");
		orderProductReq.setAttach(JsonUtil.toJsonString(distributeAttach2));
		orderProductReqList.add(orderProductReq2);
		
		registerOrderReq.setProductList(orderProductReqList);
		
		orderFeign.registerOrder(registerOrderReq);

		if (needPayAmount.compareTo(BigDecimal.ZERO) == 0) {

		}
		
		// 获取支付参数
		PayReq payReq = new PayReq();
		payReq.setUserId(userId);
		payReq.setOrderCode(orderCode);
		payReq.setBusinessType(OrderPayEnum.BusinessType.DISTRIBUTE);
		payReq.setMethod(OrderPayEnum.Method.WX);
		payReq.setAppid("wxeb6ffb3sdadda333");
		payReq.setAmount(needPayAmount);
		payReq.setBody("配送订单下单");
		payReq.setSpbillCreateIp(HttpContextUtil.requestip());
//		payReq.setProductId(productId);
		payReq.setOpenid(HttpContextUtil.deviceid());
		payReq.setNotifyUrl("http://" + Constants.FEIGNCLIENT_VALUE + "/distributeOrder/buyNotify");
//		payReq.setAttach(attach);
//		payReq.setTimeoutSeconds(timeoutSeconds);
//		payReq.setRemark(remark);
		PayResp payResp = payFeign.unifiedorder(payReq).dataOrThrow();
		if (!payResp.getSuccess()) {
			return Result.fail("");
		}
		return Result.success(payResp.getPayInfo());
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
			OrderCancelReq orderCancelReq = new OrderCancelReq();
			orderCancelReq.setOrderCode(orderCode);
			orderCancelReq.setCancelTime(time);
			orderFeign.cancel(orderCancelReq);
			
			// TODO 修改‘业务订单’数据
			
			return Result.success();
		}
		
		if (!payNotifyReq.getSuccess()) {
			// 支付失败

			// 发布‘支付失败’事件
			Map<String, Object> params = Maps.newHashMap();
			params.put("orderCode", orderCode);
			messageSender.sendFanoutMessage(params, FanoutConstants.DISTRIBUTE_PAY_FAIL.EXCHANGE);

			return Result.success();
		}
		// 支付成功
		
		// 修改‘订单中心’数据
		OrderPaySuccessReq orderPaySuccessReq = new OrderPaySuccessReq();
		orderPaySuccessReq.setOrderCode(orderCode);
		orderPaySuccessReq.setPayTime(time);
		orderFeign.paySuccess(orderPaySuccessReq);
		
		// TODO 修改‘业务订单’数据
		
    	// 发布‘支付成功’事件
		Map<String, Object> params = Maps.newHashMap();
		params.put("orderCode", orderCode);
		messageSender.sendFanoutMessage(params, FanoutConstants.DISTRIBUTE_PAY_SUCCESS.EXCHANGE);
    	
		// TODO 处理‘业务订单’逻辑
		
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

		List<ProductReq> productList = orderReq.getProductList();
		List<ProductDetailResp> productDetailRespList = productList.stream().map(v -> {
			ProductDetailResp productDetailResp = new ProductDetailResp();
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
		
		List<TextValueResp> textValueList = Lists.newArrayList();
		textValueList.add(new TextValueResp().setText("aaaaa").setValue("bbbbb"));
		resp.setTextValueList(textValueList);
		return resp;
	}
}
