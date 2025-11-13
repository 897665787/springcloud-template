package com.company.tool.retry.strategy;

import com.company.framework.context.SpringContextUtil;
import com.company.tool.api.enums.RetryerEnum;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WaitStrategyBeanFactory {
    private static final String SUFFIX = "WaitStrategy";

    public static final String INCREMENTING_WAIT_STRATEGY = RetryerEnum.WAIT_STRATEGY_INCREMENTING + SUFFIX;
    public static final String FIXED_WAIT_STRATEGY = RetryerEnum.WAIT_STRATEGY_FIXED + SUFFIX;
    public static final String EXPONENTIAL_WAIT_STRATEGY = RetryerEnum.WAIT_STRATEGY_EXPONENTIAL + SUFFIX;
    public static final String FIBONACCI_WAIT_STRATEGY = RetryerEnum.WAIT_STRATEGY_FIBONACCI + SUFFIX;
    public static final String RANDOM_WAIT_STRATEGY = RetryerEnum.WAIT_STRATEGY_RANDOM + SUFFIX;
    public static final String WECHAT_WAIT_STRATEGY = RetryerEnum.WAIT_STRATEGY_WECHAT + SUFFIX;

    public static WaitStrategy of(String waitStrategyCode) {
        String beanName = waitStrategyCode + SUFFIX;
        WaitStrategy waitStrategy = SpringContextUtil.getBean(beanName, WaitStrategy.class);
        if (waitStrategy == null) {
            log.warn("等待策略{}找不到，使用默认策略执行", waitStrategyCode);
            // 默认策略
            waitStrategy = SpringContextUtil.getBean(INCREMENTING_WAIT_STRATEGY, WaitStrategy.class);
        }
        return waitStrategy;
    }
}