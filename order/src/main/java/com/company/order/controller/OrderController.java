package com.company.order.controller;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.plugins.Page;
import com.company.common.api.Result;
import com.company.common.util.JsonUtil;
import com.company.common.util.MdcUtil;
import com.company.common.util.PropertyUtils;
import com.company.framework.amqp.MessageSender;
import com.company.framework.amqp.rabbit.constants.FanoutConstants;
import com.company.framework.context.HttpContextUtil;
import com.company.order.api.enums.OrderEnum;
import com.company.order.api.enums.OrderEnum.SubStatusEnum;
import com.company.order.api.feign.OrderFeign;
import com.company.order.api.feign.PayFeign;
import com.company.order.api.request.ChangeOrderStatusReq;
import com.company.order.api.request.OrderReq;
import com.company.order.api.request.RegisterOrderReq;
import com.company.order.api.response.OrderResp;
import com.company.order.api.response.PayInfoResp;
import com.company.order.entity.Order;
import com.company.order.entity.OrderProduct;
import com.company.order.service.OrderProductService;
import com.company.order.service.OrderService;
import com.google.common.collect.Maps;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/order")
public class OrderController implements OrderFeign {

	@Autowired
	private OrderService orderService;
	@Autowired
	private OrderProductService orderProductService;
	@Autowired
	private PayFeign payFeign;
	@Autowired
	private MessageSender messageSender;

	@Override
	public Result<OrderResp> registerOrder(@RequestBody RegisterOrderReq registerOrderReq) {
		Integer userId = registerOrderReq.getUserId();
		String orderCode = registerOrderReq.getOrderCode();
		OrderEnum.OrderType orderTypeEnum = registerOrderReq.getOrderTypeEnum();
		OrderEnum.SubStatusEnum subStatusEnum = registerOrderReq.getSubStatusEnum();
		BigDecimal productAmount = registerOrderReq.getProductAmount();
		BigDecimal orderAmount = registerOrderReq.getOrderAmount();
		BigDecimal reduceAmount = registerOrderReq.getReduceAmount();
		if (reduceAmount == null) {
			reduceAmount = BigDecimal.ZERO;
		}
		BigDecimal needPayAmount = registerOrderReq.getNeedPayAmount();
		String subOrderUrl = registerOrderReq.getSubOrderUrl();

		Order order = orderService.saveOrUpdate(userId, orderTypeEnum, orderCode, subStatusEnum, productAmount,
				orderAmount, reduceAmount, needPayAmount, subOrderUrl);

		List<OrderProduct> orderProductList = orderProductService.selectByOrderCode(orderCode);
		if (orderProductList.isEmpty()) {
			List<RegisterOrderReq.OrderProductReq> orderProductReqList = registerOrderReq.getProductList();
			if (CollectionUtils.isNotEmpty(orderProductReqList)) {
				List<OrderProduct> orderProductList4Insert = PropertyUtils.copyArrayProperties(orderProductReqList,
						OrderProduct.class);
				for (OrderProduct orderProduct : orderProductList4Insert) {
					orderProduct.setOrderCode(orderCode);
				}
				orderProductService.insertBatch(orderProductList4Insert);
			}
		}

		return Result.success(PropertyUtils.copyProperties(order, OrderResp.class));
	}

	@Override
	public Result<OrderResp> changeStatus(@RequestBody ChangeOrderStatusReq changeOrderStatusReq) {
		String orderCode = changeOrderStatusReq.getOrderCode();
		SubStatusEnum targetSubStatusEnum = changeOrderStatusReq.getTargetSubStatusEnum();
		orderService.updateStatus(orderCode, targetSubStatusEnum);
		return Result.success();
	}

	@Override
	public Result<List<OrderResp>> page(
			@Valid @NotNull(message = "缺少参数当前页") @Min(value = 1, message = "当前页不能小于1") Integer current,
			@Valid @NotNull(message = "缺少参数每页数量") Integer size, OrderEnum.StatusEnum status) {
//		Integer userId = HttpContextUtil.currentUserIdInt();
		Integer userId = 1;
		Page<Order> page = new Page<>(current, size);
		List<Order> orderList = orderService.selectByUserIdAndStatus(page, userId, status);

		List<String> orderCodeList = orderList.stream().map(Order::getOrderCode).collect(Collectors.toList());
		Map<String, List<OrderProduct>> orderCodeThisListMap = orderProductService.groupByOrderCodes(orderCodeList);
		
		String traceId = MdcUtil.get();
		List<OrderResp> orderRespList = orderList.parallelStream().map(v -> {
			String subTraceId = MdcUtil.get();
			if (subTraceId == null) {
				MdcUtil.put(traceId);
			}
			
			OrderResp orderResp = toOrderResp(v, orderCodeThisListMap);

			if (subTraceId == null) {
				MdcUtil.remove();
			}
			return orderResp;
		}).collect(Collectors.toList());
		return Result.success(orderRespList);
	}
	
	private OrderResp toOrderResp(Order order, Map<String, List<OrderProduct>> orderCodeThisListMap) {
		OrderResp orderResp = new OrderResp();
		orderResp.setOrderCode(order.getOrderCode());
		String orderType = order.getOrderType();
		orderResp.setOrderType(orderType);
		orderResp.setNeedPayAmount(order.getNeedPayAmount());
		orderResp.setPayAmount(order.getPayAmount());

		List<OrderProduct> orderProductList = orderCodeThisListMap.getOrDefault(order.getOrderCode(), Collections.emptyList());
		List<OrderResp.ProductResp> productRespList = PropertyUtils.copyArrayProperties(orderProductList,
				OrderResp.ProductResp.class);
		orderResp.setProductList(productRespList);
		
		OrderEnum.StatusEnum statusEnum = OrderEnum.StatusEnum.of(order.getStatus());
		OrderEnum.SubStatusEnum subStatusEnum = OrderEnum.SubStatusEnum.of(order.getSubStatus());

		orderResp.setStatusText(subStatusEnum.getMessage());
		if (OrderEnum.SubStatusEnum.COMPLETE == subStatusEnum) {
			orderResp.setStatusText(statusEnum.getMessage());
		}
		
		if (OrderEnum.StatusEnum.WAIT_PAY == statusEnum) {
			orderResp.setTimeText("下单时间");
			orderResp.setTime(order.getCreateTime());
			orderResp.setPayText("需付款");
		} else if (OrderEnum.StatusEnum.CANCELED == statusEnum) {
			orderResp.setTimeText("关闭时间");
			orderResp.setTime(order.getPayTime());
			orderResp.setPayText("需付款");
		} else if (OrderEnum.StatusEnum.WAIT_SEND == statusEnum || OrderEnum.StatusEnum.WAIT_RECEIVE == statusEnum) {
			orderResp.setTimeText("付款时间");
			orderResp.setTime(order.getPayTime());
			orderResp.setPayText("实付款");
		} else if (OrderEnum.StatusEnum.COMPLETE == statusEnum) {
			orderResp.setTimeText("完成时间");
			orderResp.setTime(order.getFinishTime());
			orderResp.setPayText("实付款");
		} else if (OrderEnum.SubStatusEnum.CHECK == subStatusEnum) {
			orderResp.setTimeText("完成时间");
			orderResp.setTime(order.getFinishTime());
			orderResp.setPayText("实付款");
		} else if (OrderEnum.SubStatusEnum.REFUNDING == subStatusEnum) {
			orderResp.setTimeText("付款时间");
			orderResp.setTime(order.getPayTime());
			orderResp.setPayText("实付款");
		} else if (OrderEnum.SubStatusEnum.REFUND_SUCCESS == subStatusEnum) {
			orderResp.setTimeText("退款时间");
			orderResp.setTime(order.getRefundTime());
			orderResp.setPayText("实付款");
		}

		orderResp.setCancelBtn(false);
		orderResp.setToPayBtn(false);
		if (OrderEnum.StatusEnum.WAIT_PAY == statusEnum) {// 待支付情况下需返回支付参数
			orderResp.setCancelBtn(true);
			orderResp.setToPayBtn(true);
			
			PayInfoResp payInfoResp = payFeign.queryPayInfo(order.getOrderCode()).dataOrThrow();
			orderResp.setPayMethod(payInfoResp.getMethod());
			orderResp.setPayInfo(payInfoResp.getPayInfo());
		}
		
		String subOrderUrl = order.getSubOrderUrl();

		Object data = requestSubOrder(subOrderUrl, order, orderProductList);
		orderResp.setSubOrder(data);
		
		// 如果data里面有statusText字段，则覆盖外层的statusText
		JSONObject dataJSON = JSON.parseObject(JSON.toJSONString(data));
		if (dataJSON.containsKey("statusText")) {
			orderResp.setStatusText(dataJSON.getString("statusText"));
		}
		if (dataJSON.containsKey("timeText")) {
			orderResp.setTimeText(dataJSON.getString("timeText"));
		}
		if (dataJSON.containsKey("time")) {
			LocalDateTime time = LocalDateTime.parse(dataJSON.getString("time"),
					DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
			orderResp.setTime(time);
		}
		if (dataJSON.containsKey("payText")) {
			orderResp.setPayText(dataJSON.getString("payText"));
		}

        return orderResp;
	}
	
	@Override
	public Result<OrderResp> queryByOrderCode(String orderCode) {
		Order order = orderService.selectByOrderCode(orderCode);
		if (order == null) {
			return Result.fail("订单不存在");
		}

		List<OrderProduct> orderProductList = orderProductService.selectByOrderCode(orderCode);
		Map<String, List<OrderProduct>> orderCodeThisListMap = Maps.newHashMap();
		orderCodeThisListMap.put(orderCode, orderProductList);

		OrderResp orderResp = toOrderResp(order, orderCodeThisListMap);

		return Result.success(orderResp);
	}

	@Override
	public Result<OrderResp> cancel(String orderCode) {
		Integer affect = orderService.cancel(orderCode);
		if (affect <= 0) {
			return Result.fail("取消订单失败");
		}

		Order order = orderService.selectByOrderCode(orderCode);
		
		List<OrderProduct> orderProductList = orderProductService.selectByOrderCode(orderCode);
		Map<String, List<OrderProduct>> orderCodeThisListMap = Maps.newHashMap();
		orderCodeThisListMap.put(orderCode, orderProductList);
		
		OrderResp orderResp = toOrderResp(order, orderCodeThisListMap);

		// 发布订单取消事件
		Map<String, Object> params = Maps.newHashMap();
		params.put("orderCode", orderCode);
		params.put("orderType", order.getOrderType());
		messageSender.sendFanoutMessage(params, FanoutConstants.ORDER_CANCEL.EXCHANGE);

		return Result.success(orderResp);
	}

	private Object requestSubOrder(String subOrderUrl, Order order, List<OrderProduct> orderProductList) {
		Object data = null;
		if (StringUtils.isNotBlank(subOrderUrl)) {
			long start = System.currentTimeMillis();
			OrderReq orderReq = PropertyUtils.copyProperties(order, OrderReq.class);

			List<OrderReq.ProductReq> productReqList = PropertyUtils.copyArrayProperties(orderProductList,
					OrderReq.ProductReq.class);
			orderReq.setProductList(productReqList);

			data = postRestTemplate(subOrderUrl, orderReq);
			log.info("subOrderUrl:{},orderReq:{},mills:{}", subOrderUrl, JsonUtil.toJsonString(orderReq),
					System.currentTimeMillis() - start);
		} else {
			Map<String, String> dataMap = Maps.newHashMap();
			dataMap.put("message", "未设置子订单查询地址");
			data = dataMap;
		}
		return data;
	}

	@Autowired
	private RestTemplate restTemplate;
	
	private Object postRestTemplate(String url, OrderReq orderReq) {
		Object paramObject = orderReq;
		String remark = null;
		log.info("回调,请求地址:{},原参数:{},参数:{}", url, JsonUtil.toJsonString(paramObject), JsonUtil.toJsonString(paramObject));
		long start = System.currentTimeMillis();
		try {
			HttpHeaders headers = new HttpHeaders();
			MdcUtil.headers2().forEach((k, v) -> headers.addAll(k, v));// 日志追踪ID
			HttpContextUtil.httpContextHeaders().forEach((k, v) -> headers.addAll(k, v));// 请求头
			HttpEntity<Object> httpEntity = new HttpEntity<>(paramObject, headers);
			@SuppressWarnings("rawtypes")
			ResponseEntity<Result> responseEntity = restTemplate.postForEntity(url, httpEntity,
					Result.class);
			if (responseEntity.getStatusCode() == HttpStatus.OK) {
				@SuppressWarnings("unchecked")
				Result<Object> result = responseEntity.getBody();
				log.info("{}ms,回调结果:{}", System.currentTimeMillis() - start, JsonUtil.toJsonString(result));
				remark = result.getMessage();
				if (result.successCode()) {
					return result.getData();
				}
			} else {
				remark = "响应码:" + responseEntity.getStatusCodeValue();
			}
		} catch (Exception e) {
			log.error("{}ms,回调异常", System.currentTimeMillis() - start, e);
			remark = ExceptionUtils.getMessage(e);
		}
		Map<String, String> dataMap = Maps.newHashMap();
		dataMap.put("message", remark);
		return dataMap;
	}
}
