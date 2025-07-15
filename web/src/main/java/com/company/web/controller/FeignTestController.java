package com.company.web.controller;

import com.company.common.api.Result;
import com.company.framework.util.JsonUtil;
import com.company.framework.util.PropertyUtils;
import com.company.order.api.feign.FeignTestFeign;
import com.company.order.api.request.RegisterOrderReq;
import com.company.order.api.response.OrderDetailResp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/feignTest")
@Slf4j
public class FeignTestController {

    @Autowired
    private FeignTestFeign feignTestFeign;

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping(value = "/getnoparam")
    public Result<com.company.web.resp.OrderDetailResp> getnoparam() {
		OrderDetailResp orderDetailResp = feignTestFeign.getnoparam().dataOrThrow();
		com.company.web.resp.OrderDetailResp resp = PropertyUtils.copyProperties(orderDetailResp, com.company.web.resp.OrderDetailResp.class);
		return Result.success(resp);
    }

    @GetMapping(value = "/getparam")
    public Result<com.company.web.resp.OrderDetailResp> getparam(String orderCode) {
        Result result = restTemplate.getForObject("http://template-order/feignTest/getparam?orderCode=" + orderCode, Result.class);
        log.info("result:{}", JsonUtil.toJsonString(result));

		OrderDetailResp orderDetailResp = feignTestFeign.getparam(orderCode).dataOrThrow();
		com.company.web.resp.OrderDetailResp resp = PropertyUtils.copyProperties(orderDetailResp, com.company.web.resp.OrderDetailResp.class);
		return Result.success(resp);
    }

    @GetMapping(value = "/postbody")
    public Result<com.company.web.resp.OrderDetailResp> postbody(@RequestBody RegisterOrderReq registerOrderReq) {
        Result result = restTemplate.postForObject("http://template-order/feignTest/postbody",registerOrderReq, Result.class);
        log.info("result:{}", JsonUtil.toJsonString(result));

        OrderDetailResp orderDetailResp = feignTestFeign.postbody(registerOrderReq).dataOrThrow();
		com.company.web.resp.OrderDetailResp resp = PropertyUtils.copyProperties(orderDetailResp, com.company.web.resp.OrderDetailResp.class);
		return Result.success(resp);
    }
}
