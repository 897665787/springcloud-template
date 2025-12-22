package com.company.order.controller;

import com.company.framework.context.HeaderContextUtil;
import com.company.framework.gracefulresponse.feign.GracefulResponseFeignExceptionAspect;
import com.company.framework.util.JsonUtil;
import com.company.order.api.feign.FeignTestFeign;
import com.company.order.api.request.RegisterOrderReq;
import com.company.order.api.response.OrderDetailResp;
import com.feiniaojin.gracefulresponse.GracefulResponse;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/feignTest")
public class FeignTestController implements FeignTestFeign {

	private final GracefulResponseFeignExceptionAspect gracefulResponseFeignExceptionAspect;

	public FeignTestController(GracefulResponseFeignExceptionAspect gracefulResponseFeignExceptionAspect) {
		this.gracefulResponseFeignExceptionAspect = gracefulResponseFeignExceptionAspect;
	}

	@Override
	public OrderDetailResp getnoparam() {
		if (true) {
			GracefulResponse.raiseException("500", "测试feign异常");
		}
		OrderDetailResp resp = new OrderDetailResp();
		resp.setOrderCode("123456");
		resp.setOrderType("normal");
		resp.setPayAmount(new BigDecimal("11.00"));
		return resp;
	}

    @Override
    public List<OrderDetailResp> getnoparamList() {
        OrderDetailResp resp = new OrderDetailResp();
        resp.setOrderCode("123456");
        resp.setOrderType("normal");
        resp.setPayAmount(new BigDecimal("11.00"));
        return Lists.newArrayList(resp);
    }

	@Override
	public OrderDetailResp getparam(String orderCode) {
		OrderDetailResp resp = new OrderDetailResp();
		resp.setOrderCode(orderCode);
		resp.setOrderType("normal");
		resp.setPayAmount(new BigDecimal("100.00"));
		return resp;
	}

	@Override
	public OrderDetailResp postbody(RegisterOrderReq registerOrderReq) {
		OrderDetailResp resp = new OrderDetailResp();
		resp.setOrderCode(registerOrderReq.getOrderCode());
		resp.setOrderType(registerOrderReq.getOrderType());
		resp.setPayAmount(registerOrderReq.getOrderAmount());
		return resp;
	}

	@Override
	public OrderDetailResp context() {
		log.info("HeaderContextUtil.currentUserId:{}", HeaderContextUtil.currentUserId());
		log.info("HeaderContextUtil.headerMap:{}", JsonUtil.toJsonString(HeaderContextUtil.headerMap()));
		OrderDetailResp resp = new OrderDetailResp();
		return resp;
	}
}
