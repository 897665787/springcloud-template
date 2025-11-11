package com.company.web.controller;

import com.company.common.api.Result;
import com.company.framework.lock.annotation.Lock;
import com.company.framework.util.JsonUtil;
import com.company.order.api.request.RegisterOrderReq;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/lock")
@Slf4j
public class LockController {

    @Lock("lock:getnoparam")
    @GetMapping(value = "/getnoparam")
    public Result<Map<String, Object>> getnoparam() {
        System.out.println("getnoparam");
        return Result.success();
    }

    @Lock("'lock:getnoparam:'+#orderCode")
    @GetMapping(value = "/getparam")
    public Result<Map<String, Object>> getparam(String orderCode) {
        System.out.println("getparam:" + orderCode);
        return Result.success();
    }

    @Lock("'lock:getnoparam:'+#registerOrderReq.orderCode")
    @GetMapping(value = "/postbody")
    public Result<Map<String, Object>> postbody(@RequestBody RegisterOrderReq registerOrderReq) {
        System.out.println("postbody:" + JsonUtil.toJsonString(registerOrderReq));
        return Result.success();
    }
}
