package com.company.tool.controller;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.company.common.api.Result;
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
import com.company.order.api.request.PayNotifyReq;
import com.company.order.api.request.PayReq;
import com.company.order.api.request.RegisterOrderReq;
import com.company.order.api.response.PayResp;
import com.company.tool.api.feign.SubOrderDemo3Feign;
import com.company.tool.api.request.SubOrderDemo3OrderReq;
import com.company.tool.api.response.SubOrderDemo3SubOrderDetailResp;
import com.company.tool.api.response.SubOrderDemo3SubOrderResp;
import com.company.user.api.constant.Constants;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import lombok.extern.slf4j.Slf4j;

/**
 * 子订单demo3(表示不同的业务子订单)
 */
@Slf4j
@RestController
@RequestMapping("/subOrderDemo3")
public class SubOrderDemo3Controller implements SubOrderDemo3Feign {
	
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
	 * @param subOrderDemo3OrderReq
	 * @return
	 */
	@Override
	public Result<?> buy(@RequestBody SubOrderDemo3OrderReq subOrderDemo3OrderReq) {
		Integer userId = HttpContextUtil.currentUserIdInt();
		// 参数校验
		BigDecimal orderAmount = subOrderDemo3OrderReq.getOrderAmount();
		BigDecimal payAmount = subOrderDemo3OrderReq.getPayAmount();
		BigDecimal needPayAmount = new BigDecimal("17.5");
		if (payAmount.compareTo(needPayAmount) != 0) {
			return Result.fail("");
		}
		
		// 条件校验（下单限制、风控）
		

		// 创建业务订单（订单中心子订单）
		String orderCode = String.valueOf(sequenceGenerator.nextId());

		// 注册到‘订单中心’
		BigDecimal productAmount = new BigDecimal("20");
		BigDecimal reduceAmount = new BigDecimal("2.5");
		
		RegisterOrderReq registerOrderReq = new RegisterOrderReq();
		registerOrderReq.setUserId(userId);
		registerOrderReq.setOrderCode(orderCode);
		registerOrderReq.setOrderTypeEnum(OrderEnum.OrderType.SUBORDERDEMO3);
		registerOrderReq.setSubStatusEnum(OrderEnum.SubStatusEnum.WAIT_PAY);
		registerOrderReq.setProductAmount(productAmount);
		registerOrderReq.setOrderAmount(orderAmount);
		registerOrderReq.setReduceAmount(reduceAmount);
		registerOrderReq.setNeedPayAmount(needPayAmount);
		registerOrderReq.setSubOrderUrl("http://" + Constants.FEIGNCLIENT_VALUE + "/subOrderDemo3/subOrder");
		
		List<RegisterOrderReq.OrderProductReq> orderProductReqList = Lists.newArrayList();

		RegisterOrderReq.OrderProductReq orderProductReq = new RegisterOrderReq.OrderProductReq();
		orderProductReq.setNumber(1);
		orderProductReq.setOriginAmount(new BigDecimal("20"));
		orderProductReq.setSalesAmount(new BigDecimal("20"));
		orderProductReq.setProductCode("XG3313213131");
		orderProductReq.setProductName("子订单demo3");
		orderProductReq.setProductImage("http://www.image.com/demo3_month.png");
		
		orderProductReqList.add(orderProductReq);

		registerOrderReq.setProductList(orderProductReqList);
		
		orderFeign.registerOrder(registerOrderReq);

		if (needPayAmount.compareTo(BigDecimal.ZERO) == 0) {
			return Result.fail("");
		}
		
		// 获取支付参数
		PayReq payReq = new PayReq();
		payReq.setUserId(userId);
		payReq.setOrderCode(orderCode);
		payReq.setBusinessType(OrderPayEnum.BusinessType.SUBORDERDEMO3);
		payReq.setMethod(OrderPayEnum.Method.WX);
		payReq.setAppid("wxeb6ffb3sdadda333");
		payReq.setAmount(needPayAmount);
		payReq.setBody("子订单demo3");
		payReq.setSpbillCreateIp(HttpContextUtil.requestip());
//		payReq.setProductId(productId);
		payReq.setOpenid(HttpContextUtil.deviceid());
		payReq.setNotifyUrl("http://" + Constants.FEIGNCLIENT_VALUE + "/subOrderDemo3/buyNotify");
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
			messageSender.sendFanoutMessage(params, FanoutConstants.SUBORDERDEMO3_PAY_FAIL.EXCHANGE);

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
		messageSender.sendFanoutMessage(params, FanoutConstants.SUBORDERDEMO3_PAY_SUCCESS.EXCHANGE);
    	
		// TODO 子订单demo3业务逻辑
		
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

	private SubOrderDemo3SubOrderResp item(OrderReq orderReq) {
		SubOrderDemo3SubOrderResp resp = new SubOrderDemo3SubOrderResp();
		
		resp.setPayText("已付款（覆盖文案）");
		
		return resp;
	}
	
	private SubOrderDemo3SubOrderDetailResp detail(OrderReq orderReq) {
		SubOrderDemo3SubOrderDetailResp resp = new SubOrderDemo3SubOrderDetailResp();
		
		resp.setPayText("已付款（覆盖文案）");
		
		return resp;
	}
}
