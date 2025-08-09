package com.company.order.controller;

import com.company.common.api.Result;
import com.company.framework.context.HeaderContextUtil;
import com.company.framework.util.JsonUtil;
import com.company.order.api.feign.FeignTestFeign;
import com.company.order.api.request.RegisterOrderReq;
import com.company.order.api.response.OrderDetailResp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@Slf4j
@RequestMapping("/feignTest")
public class FeignTestController implements FeignTestFeign {

	@Override
	public Result<OrderDetailResp> getnoparam() {
		OrderDetailResp resp = new OrderDetailResp();
		resp.setOrderCode("123456");
		resp.setOrderType("normal");
		resp.setPayAmount(new BigDecimal("11.00"));
		return Result.success(resp);
	}

	@Override
	public Result<OrderDetailResp> getparam(String orderCode) {
		OrderDetailResp resp = new OrderDetailResp();
		resp.setOrderCode(orderCode);
		resp.setOrderType("normal");
		resp.setPayAmount(new BigDecimal("100.00"));
		return Result.success(resp);
	}

	@Override
	public Result<OrderDetailResp> postbody(RegisterOrderReq registerOrderReq) {
		OrderDetailResp resp = new OrderDetailResp();
		resp.setOrderCode(registerOrderReq.getOrderCode());
		resp.setOrderType(registerOrderReq.getOrderType());
		resp.setPayAmount(registerOrderReq.getOrderAmount());
		return Result.success(resp);
	}

	@Override
	public Result<OrderDetailResp> context() {
		log.info("HeaderContextUtil.currentUserId:{}", HeaderContextUtil.currentUserId());
		log.info("HeaderContextUtil.headerMap:{}", JsonUtil.toJsonString(HeaderContextUtil.headerMap()));
		OrderDetailResp resp = new OrderDetailResp();
		return Result.success(resp);
	}
}
