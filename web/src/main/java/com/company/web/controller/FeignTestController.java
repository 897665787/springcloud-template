package com.company.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.company.order.api.feign.FeignTestFeign;
import com.company.order.api.request.RegisterOrderReq;
import com.company.order.api.response.OrderDetailResp;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/feignTest")
@Slf4j
public class FeignTestController {

    @Autowired
    private FeignTestFeign feignTestFeign;

    @GetMapping(value = "/getnoparam")
    public OrderDetailResp getnoparam() {
        return feignTestFeign.getnoparam();
    }

    @GetMapping(value = "/getparam")
    public OrderDetailResp getparam(String orderCode) {
        return feignTestFeign.getparam(orderCode);
    }

    @GetMapping(value = "/postbody")
    public OrderDetailResp postbody(@RequestBody RegisterOrderReq registerOrderReq) {
        return feignTestFeign.postbody(registerOrderReq);
    }
}
