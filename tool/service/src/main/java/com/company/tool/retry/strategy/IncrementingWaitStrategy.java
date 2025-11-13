package com.company.tool.retry.strategy;

import org.springframework.stereotype.Component;

/**
 * 线性递增策略（默认策略）
 *
 * @author JQ棣
 */
@Component(WaitStrategyBeanFactory.INCREMENTING_WAIT_STRATEGY)
public class IncrementingWaitStrategy implements WaitStrategy {

    @Override
    public int nextSeconds(int increaseSeconds, int failure) {
        return increaseSeconds * (failure + 1);
    }

    public static void main(String[] args) {
        IncrementingWaitStrategy strategy = new IncrementingWaitStrategy();
        for (int i = 0; i < 10; i++) {
            System.out.println("Failure: " + i + ", Wait Seconds: " + strategy.nextSeconds(2, i));
        }
    }

}