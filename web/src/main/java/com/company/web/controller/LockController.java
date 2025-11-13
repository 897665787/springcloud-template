package com.company.web.controller;

import com.company.common.api.Result;
import com.company.framework.lock.LockClient;
import com.company.framework.lock.annotation.Lock;
import com.company.framework.util.JsonUtil;
import com.company.order.api.request.RegisterOrderReq;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/lock")
@Slf4j
public class LockController {
    @Autowired
    private LockClient lockClient;

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

    @GetMapping(value = "/lockclient")
    public Result<Map<String, Object>> lockclient(String orderCode) {
        System.out.println("编程式锁，粒度更细");
        String key = "lock:lockclient:" + orderCode;
        Map<String, Object> result = lockClient.doInLock(key, () -> {
            System.out.println("lockclient:" + key);
            Map<String, Object> map = Maps.newHashMap();
            map.put("orderCode", orderCode);
            return map;
        });
        return Result.success(result);
    }
}
