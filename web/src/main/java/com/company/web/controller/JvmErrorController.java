package com.company.web.controller;

import com.company.common.api.Result;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/jvmerror")
@Slf4j
public class JvmErrorController {

    Map<String, Object> map = Maps.newHashMap();
    // 触发OOM
    @GetMapping(value = "/dump")
    public Result<Map<String, Object>> dump() {
        for (int i = 0; i < 10000000; i++) {
            byte[] bytes = new byte[1024 * 1024];
            map.put("" + i, bytes);
            log.info("i:{}", i);
        }
        return Result.success(map);
    }

    // 触发StackOverflowError
    @GetMapping(value = "/stackOverflow")
    public Result<Map<String, Object>> stackOverflow() {
        // 没有dump
        return stackOverflow();
    }

    // 触发-XX:ErrorFile=./logs/hs_err_pid-%p.log
    // kill -11 pid
}
