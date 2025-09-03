package com.company.tool.api.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

public interface RetryerEnum {

    String WAIT_STRATEGY_INCREMENTING = "incrementing"; // 线性递增
    String WAIT_STRATEGY_FIXED = "fixed"; // 固定等待
    String WAIT_STRATEGY_EXPONENTIAL = "exponential";// 指数退避
    String WAIT_STRATEGY_FIBONACCI = "fibonacci";// 斐波那契
    String WAIT_STRATEGY_RANDOM = "random";// 随机
    String WAIT_STRATEGY_WECHAT = "wechat";// 微信

    @AllArgsConstructor
    enum SecondsStrategy {
        INCREMENTING(WAIT_STRATEGY_INCREMENTING, "线性递增"),
        FIX(WAIT_STRATEGY_FIXED, "固定等待"),
        EXPONENTIAL(WAIT_STRATEGY_EXPONENTIAL, "指数退避"),
        FIBONACCI(WAIT_STRATEGY_FIBONACCI, "斐波那契"),
        RANDOM(WAIT_STRATEGY_RANDOM, "随机"),
        WECHAT(WAIT_STRATEGY_WECHAT, "微信"),
        ;

        @Getter
        private String code;
        @Getter
        private String desc;

        public static SecondsStrategy of(String code) {
            for (SecondsStrategy item : SecondsStrategy.values()) {
                if (item.getCode().equals(code)) {
                    return item;
                }
            }
            return null;
        }
    }
}
