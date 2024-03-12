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
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.plugins.Page;
import com.company.common.api.Result;
import com.company.common.util.JsonUtil;
import com.company.common.util.MdcUtil;
import com.company.common.util.PropertyUtils;
import com.company.framework.context.HttpContextUtil;
import com.company.order.api.enums.OrderEnum;
import com.company.order.api.feign.OrderFeign;
import com.company.order.api.request.OrderCancelReq;
import com.company.order.api.request.OrderFinishReq;
import com.company.order.api.request.OrderPaySuccessReq;
import com.company.order.api.request.OrderReq;
import com.company.order.api.request.RegisterOrderReq;
import com.company.order.api.response.OrderDetailResp;
import com.company.order.api.response.OrderDetailResp.TextValueResp;
import com.company.order.api.response.OrderResp;
import com.company.order.entity.Order;
import com.company.order.entity.OrderProduct;
import com.company.order.service.OrderProductService;
import com.company.order.service.OrderService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import cn.hutool.core.date.LocalDateTimeUtil;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/order")
public class OrderController implements OrderFeign {

	@Autowired
	private OrderService orderService;
	@Autowired
	private OrderProductService orderProductService;

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
		String attach = registerOrderReq.getAttach();

		Order order = orderService.saveOrUpdate(userId, orderTypeEnum, orderCode, subStatusEnum, productAmount,
				orderAmount, reduceAmount, needPayAmount, subOrderUrl, attach);

		List<OrderProduct> orderProductList = orderProductService.selectByOrderCode(orderCode);
		if (orderProductList.isEmpty()) {
			List<RegisterOrderReq.OrderProductReq> orderProductReqList = registerOrderReq.getProductList();
			if (CollectionUtils.isNotEmpty(orderProductReqList)) {
				List<OrderProduct> orderProductList4Insert = orderProductReqList.stream().map(v -> {
					OrderProduct orderProduct = new OrderProduct();
					orderProduct.setOrderCode(orderCode);
					orderProduct.setNumber(v.getNumber());
					orderProduct.setOriginAmount(v.getOriginAmount());
					orderProduct.setSalesAmount(v.getSalesAmount());
					orderProduct.setAmount(v.getSalesAmount().multiply(new BigDecimal(v.getNumber())));
					orderProduct.setProductCode(v.getProductCode());
					orderProduct.setProductName(v.getProductName());
					orderProduct.setProductImage(v.getProductImage());
					orderProduct.setAttach(v.getAttach());
					return orderProduct;
				}).collect(Collectors.toList());
				orderProductService.insertBatch(orderProductList4Insert);
			}
		}

		return Result.success(PropertyUtils.copyProperties(order, OrderResp.class));
	}

	@Override
	public Result<OrderDetailResp> cancelByUser(@RequestBody OrderCancelReq orderCancelReq) {
		String orderCode = orderCancelReq.getOrderCode();
		LocalDateTime cancelTime = orderCancelReq.getCancelTime();
		Integer affect = orderService.cancel(orderCode, cancelTime);
		if (affect == 0) {
			return Result.fail("取消订单失败，请刷新订单");
		}

		Order order = orderService.selectByOrderCode(orderCode);
		String subOrderUrl = order.getSubOrderUrl();
		List<OrderProduct> orderProductList = orderProductService.selectByOrderCode(order.getOrderCode());
		
		OrderDetailResp orderDetailResp = toOrderDetailResp(order);
		Object data = requestSubOrder(OrderEnum.SubOrderEventEnum.USER_CANCEL, subOrderUrl, order, orderProductList);
		orderDetailResp.setSubOrder(data);

		return Result.success(orderDetailResp);
	}
	
	@Override
	public Result<Void> cancelByTimeout(@RequestBody OrderCancelReq orderCancelReq) {
		String orderCode = orderCancelReq.getOrderCode();
		LocalDateTime cancelTime = orderCancelReq.getCancelTime();
		Integer affect = orderService.cancel(orderCode, cancelTime);
		if (affect == 0) {
			return Result.fail("取消订单失败");
		}
		return Result.success();
	}

	@Override
	public Result<Void> paySuccess(@RequestBody OrderPaySuccessReq orderPaySuccessReq) {
		String orderCode = orderPaySuccessReq.getOrderCode();
		LocalDateTime payTime = orderPaySuccessReq.getPayTime();

		int affect = orderService.paySuccess(orderCode, payTime);
		if (affect == 0) {
			return Result.fail("修改为支付成功失败");
		}
		return Result.success();
	}

	@Override
	public Result<Void> finish(@RequestBody OrderFinishReq orderFinishReq) {
		String orderCode = orderFinishReq.getOrderCode();
		LocalDateTime finishTime = orderFinishReq.getFinishTime();

		int affect = orderService.finish(orderCode, finishTime);
		if (affect == 0) {
			return Result.fail("修改为完成失败");
		}
		return Result.success();
	}

	@Override
	public Result<List<OrderResp>> page(
			@Valid @NotNull(message = "缺少参数当前页") @Min(value = 1, message = "当前页不能小于1") Integer current,
			@Valid @NotNull(message = "缺少参数每页数量") Integer size, OrderEnum.StatusEnum status) {
		Integer userId = HttpContextUtil.currentUserIdInt();
		Page<Order> page = new Page<>(current, size);
		List<Order> orderList = orderService.pageByUserIdAndStatus(page, userId, status);

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

		List<OrderProduct> orderProductList = orderCodeThisListMap.getOrDefault(order.getOrderCode(),
				Collections.emptyList());
		List<OrderResp.ProductResp> productRespList = PropertyUtils.copyArrayProperties(orderProductList,
				OrderResp.ProductResp.class);
		orderResp.setProductList(productRespList);

		OrderEnum.StatusEnum statusEnum = OrderEnum.StatusEnum.of(order.getStatus());
		OrderEnum.SubStatusEnum subStatusEnum = OrderEnum.SubStatusEnum.of(order.getSubStatus());

		orderResp.setStatusText(subStatusEnum.getMessage());
		if (OrderEnum.SubStatusEnum.COMPLETE == subStatusEnum) {
			orderResp.setStatusText(statusEnum.getMessage());
		}

		List<OrderResp.BottonResp> bottonList = Lists.newArrayList();
		if (OrderEnum.StatusEnum.WAIT_PAY == statusEnum) {
			orderResp.setTimeText("下单时间");
			orderResp.setTime(order.getCreateTime());
			orderResp.setPayText("需付款");
			orderResp.setPayAmount(order.getNeedPayAmount());
		} else if (OrderEnum.StatusEnum.CANCELED == statusEnum) {
			orderResp.setTimeText("关闭时间");
			orderResp.setTime(order.getPayTime());
			orderResp.setPayText("需付款");
			orderResp.setPayAmount(order.getNeedPayAmount());
		} else if (OrderEnum.StatusEnum.WAIT_SEND == statusEnum || OrderEnum.StatusEnum.WAIT_RECEIVE == statusEnum) {
			orderResp.setTimeText("付款时间");
			orderResp.setTime(order.getPayTime());
			orderResp.setPayText("实付款");
			orderResp.setPayAmount(order.getPayAmount());
		} else if (OrderEnum.StatusEnum.COMPLETE == statusEnum) {
			orderResp.setTimeText("完成时间");
			orderResp.setTime(order.getFinishTime());
			orderResp.setPayText("实付款");
			orderResp.setPayAmount(order.getPayAmount());
			if (OrderEnum.SubStatusEnum.WAIT_REVIEW == subStatusEnum) {
				bottonList.add(new OrderResp.BottonResp("评价", "comment", "orderCode=" + order.getOrderCode(), 20));
			}
		} else if (OrderEnum.SubStatusEnum.CHECK == subStatusEnum) {
			orderResp.setTimeText("完成时间");
			orderResp.setTime(order.getFinishTime());
			orderResp.setPayText("实付款");
			orderResp.setPayAmount(order.getPayAmount());
		} else if (OrderEnum.SubStatusEnum.REFUNDING == subStatusEnum) {
			orderResp.setTimeText("付款时间");
			orderResp.setTime(order.getPayTime());
			orderResp.setPayText("实付款");
			orderResp.setPayAmount(order.getPayAmount());
		} else if (OrderEnum.SubStatusEnum.REFUND_SUCCESS == subStatusEnum) {
			orderResp.setTimeText("退款时间");
			orderResp.setTime(order.getRefundTime());
			orderResp.setPayText("实付款");
			orderResp.setPayAmount(order.getPayAmount());
		}
		
		orderResp.setCancelBtn(false);
		if (OrderEnum.StatusEnum.WAIT_PAY == statusEnum) {// 待支付情况下需返回支付参数
			orderResp.setCancelBtn(true);
			bottonList.add(new OrderResp.BottonResp("去支付", "topay", "orderCode=" + order.getOrderCode(), 10));
		}
		
		orderResp.setDeleteBtn(false);
		if (OrderEnum.SubStatusEnum.CANCELED == subStatusEnum || OrderEnum.SubStatusEnum.COMPLETE == subStatusEnum
				|| OrderEnum.SubStatusEnum.REFUND_SUCCESS == subStatusEnum) {// 终态才可以删除订单、再来一单
			orderResp.setDeleteBtn(true);
			String firstProductCode = orderProductList.stream().map(OrderProduct::getProductCode).findFirst().orElse("");
			bottonList.add(new OrderResp.BottonResp("再来一单", "onemore", "productCode=" + firstProductCode, 10));
		}

		String subOrderUrl = order.getSubOrderUrl();

		Object data = requestSubOrder(OrderEnum.SubOrderEventEnum.QUERY_ITEM, subOrderUrl, order, orderProductList);
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

		// 如果data里面有textValueList字段，则追加到外层的textValueList
		if (dataJSON.containsKey("bottonList")) {
			JSONArray dataJSONArray = dataJSON.getJSONArray("bottonList");
			for (int i = 0; i < dataJSONArray.size(); i++) {
				JSONObject textValueJSON = dataJSONArray.getJSONObject(i);
				String text = textValueJSON.getString("text");
				String pageKey = textValueJSON.getString("pageKey");
				String params = textValueJSON.getString("params");
				Integer sort = textValueJSON.getInteger("sort");
				bottonList.add(new OrderResp.BottonResp(text, pageKey, params, sort));
			}
		}
		
		bottonList.sort((a, b) -> Integer.compare(b.getSort(), a.getSort()));
		orderResp.setBottonList(bottonList);
		
		return orderResp;
	}

	@Override
	public Result<OrderDetailResp> queryByOrderCode(String orderCode) {
		Integer userId = HttpContextUtil.currentUserIdInt();
		Order order = orderService.selectByOrderCode(orderCode);
		if (order == null) {
			return Result.fail("订单不存在");
		}
		if (!order.getUserId().equals(userId)) {
			return Result.fail("订单不匹配");
		}

		OrderDetailResp orderDetailResp = toOrderDetailResp(order);

		return Result.success(orderDetailResp);
	}

	private OrderDetailResp toOrderDetailResp(Order order) {
		OrderDetailResp orderDetailResp = new OrderDetailResp();
		orderDetailResp.setOrderCode(order.getOrderCode());
		String orderType = order.getOrderType();
		orderDetailResp.setOrderType(orderType);

		List<OrderProduct> orderProductList = orderProductService.selectByOrderCode(order.getOrderCode());
		List<OrderDetailResp.ProductResp> productRespList = PropertyUtils.copyArrayProperties(orderProductList,
				OrderDetailResp.ProductResp.class);
		orderDetailResp.setProductList(productRespList);

		OrderEnum.StatusEnum statusEnum = OrderEnum.StatusEnum.of(order.getStatus());
		OrderEnum.SubStatusEnum subStatusEnum = OrderEnum.SubStatusEnum.of(order.getSubStatus());

		orderDetailResp.setStatusText(subStatusEnum.getMessage());
		if (OrderEnum.SubStatusEnum.COMPLETE == subStatusEnum) {
			orderDetailResp.setStatusText(statusEnum.getMessage());
		}

		if (OrderEnum.StatusEnum.WAIT_PAY == statusEnum) {
			orderDetailResp.setPayText("需付款");
			orderDetailResp.setPayAmount(order.getNeedPayAmount());
		} else if (OrderEnum.StatusEnum.CANCELED == statusEnum) {
			orderDetailResp.setPayText("需付款");
			orderDetailResp.setPayAmount(order.getNeedPayAmount());
		} else if (OrderEnum.StatusEnum.WAIT_SEND == statusEnum || OrderEnum.StatusEnum.WAIT_RECEIVE == statusEnum) {
			orderDetailResp.setPayText("实付款");
			orderDetailResp.setPayAmount(order.getPayAmount());
		} else if (OrderEnum.StatusEnum.COMPLETE == statusEnum) {
			orderDetailResp.setPayText("实付款");
			orderDetailResp.setPayAmount(order.getPayAmount());
		} else if (OrderEnum.SubStatusEnum.CHECK == subStatusEnum) {
			orderDetailResp.setPayText("实付款");
			orderDetailResp.setPayAmount(order.getPayAmount());
		} else if (OrderEnum.SubStatusEnum.REFUNDING == subStatusEnum) {
			orderDetailResp.setPayText("实付款");
			orderDetailResp.setPayAmount(order.getPayAmount());
		} else if (OrderEnum.SubStatusEnum.REFUND_SUCCESS == subStatusEnum) {
			orderDetailResp.setPayText("实付款");
			orderDetailResp.setPayAmount(order.getPayAmount());
		}

		orderDetailResp.setCancelBtn(false);
		orderDetailResp.setToPayBtn(false);
		if (OrderEnum.StatusEnum.WAIT_PAY == statusEnum) {// 待支付情况下需返回支付参数
			orderDetailResp.setCancelBtn(true);
			orderDetailResp.setToPayBtn(true);
		}

		List<OrderDetailResp.TextValueResp> textValueList = Lists.newArrayList();
		textValueList.add(
				new TextValueResp("下单时间", LocalDateTimeUtil.formatNormal(order.getCreateTime())));
		if (OrderEnum.StatusEnum.CANCELED == statusEnum) {
			textValueList.add(
					new TextValueResp("关闭时间", LocalDateTimeUtil.formatNormal(order.getPayTime())));
		} else if (OrderEnum.StatusEnum.WAIT_SEND == statusEnum || OrderEnum.StatusEnum.WAIT_RECEIVE == statusEnum) {
			textValueList.add(
					new TextValueResp("付款时间", LocalDateTimeUtil.formatNormal(order.getPayTime())));
		} else if (OrderEnum.StatusEnum.COMPLETE == statusEnum) {
			textValueList.add(
					new TextValueResp("付款时间", LocalDateTimeUtil.formatNormal(order.getPayTime())));
			textValueList.add(new TextValueResp("完成时间", LocalDateTimeUtil.formatNormal(order.getFinishTime())));
		} else if (OrderEnum.SubStatusEnum.CHECK == subStatusEnum) {
			textValueList.add(
					new TextValueResp("付款时间", LocalDateTimeUtil.formatNormal(order.getPayTime())));
			textValueList.add(new TextValueResp("申请退款时间", LocalDateTimeUtil.formatNormal(order.getFinishTime())));
		} else if (OrderEnum.SubStatusEnum.REFUNDING == subStatusEnum) {
			textValueList.add(
					new TextValueResp("付款时间", LocalDateTimeUtil.formatNormal(order.getPayTime())));
			textValueList.add(new TextValueResp("申请退款时间", LocalDateTimeUtil.formatNormal(order.getFinishTime())));
		} else if (OrderEnum.SubStatusEnum.REFUND_SUCCESS == subStatusEnum) {
			textValueList.add(
					new TextValueResp("付款时间", LocalDateTimeUtil.formatNormal(order.getPayTime())));
			textValueList.add(new TextValueResp("退款时间", LocalDateTimeUtil.formatNormal(order.getRefundTime())));
			textValueList
					.add(new TextValueResp("退款金额", order.getRefundAmount().toPlainString() + "元"));
		}

		orderDetailResp.setTextValueList(textValueList);

		String subOrderUrl = order.getSubOrderUrl();

		Object data = requestSubOrder(OrderEnum.SubOrderEventEnum.QUERY_DETAIL, subOrderUrl, order, orderProductList);
		orderDetailResp.setSubOrder(data);

		// 如果data里面有statusText字段，则覆盖外层的statusText
		JSONObject dataJSON = JSON.parseObject(JSON.toJSONString(data));
		if (dataJSON.containsKey("statusText")) {
			orderDetailResp.setStatusText(dataJSON.getString("statusText"));
		}
		// 如果data里面有textValueList字段，则追加到外层的textValueList
		if (dataJSON.containsKey("textValueList")) {
			JSONArray dataJSONArray = dataJSON.getJSONArray("textValueList");
			for (int i = 0; i < dataJSONArray.size(); i++) {
				JSONObject textValueJSON = dataJSONArray.getJSONObject(i);
				String text = textValueJSON.getString("text");
				String value = textValueJSON.getString("value");
				textValueList.add(new TextValueResp(text, value));
			}
		}
		if (dataJSON.containsKey("payText")) {
			orderDetailResp.setPayText(dataJSON.getString("payText"));
		}

		return orderDetailResp;
	}

	private Object requestSubOrder(OrderEnum.SubOrderEventEnum subOrderEvent, String subOrderUrl, Order order, List<OrderProduct> orderProductList) {
		Object data = null;
		if (StringUtils.isNotBlank(subOrderUrl)) {
			long start = System.currentTimeMillis();
			OrderReq orderReq = PropertyUtils.copyProperties(order, OrderReq.class);
			orderReq.setSubOrderEvent(subOrderEvent);
			orderReq.setStatus(OrderEnum.StatusEnum.of(order.getStatus()));
			orderReq.setSubStatus(OrderEnum.SubStatusEnum.of(order.getSubStatus()));
			
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
		log.info("请求地址:{},原参数:{},参数:{}", url, JsonUtil.toJsonString(paramObject),
				JsonUtil.toJsonString(paramObject));
		long start = System.currentTimeMillis();
		try {
			HttpHeaders headers = new HttpHeaders();
			MdcUtil.headers2().forEach((k, v) -> headers.addAll(k, v));// 日志追踪ID
			HttpContextUtil.httpContextHeaders().forEach((k, v) -> headers.addAll(k, v));// 请求头
			HttpEntity<Object> httpEntity = new HttpEntity<>(paramObject, headers);
			@SuppressWarnings("rawtypes")
			ResponseEntity<Result> responseEntity = restTemplate.postForEntity(url, httpEntity, Result.class);
			if (responseEntity.getStatusCode() == HttpStatus.OK) {
				@SuppressWarnings("unchecked")
				Result<Object> result = responseEntity.getBody();
				log.info("{}ms,结果:{}", System.currentTimeMillis() - start, JsonUtil.toJsonString(result));
				remark = result.getMessage();
				if (result.successCode()) {
					return result.getData();
				}
			} else {
				remark = "响应码:" + responseEntity.getStatusCodeValue();
			}
		} catch (Exception e) {
			log.error("{}ms,异常", System.currentTimeMillis() - start, e);
			remark = ExceptionUtils.getMessage(e);
		}
		Map<String, String> dataMap = Maps.newHashMap();
		dataMap.put("message", remark);
		return dataMap;
	}
}
