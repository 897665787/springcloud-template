package com.company.tool.retry.strategy;

import org.springframework.stereotype.Component;

/**
 * 固定等待策略
 * 
 * @author JQ棣
 */
@Component(WaitStrategyBeanFactory.FIXED_WAIT_STRATEGY)
public class FixedWaitStrategy implements WaitStrategy {

	@Override
	public int nextSeconds(int increaseSeconds, int failure) {
		return increaseSeconds;
	}

	public static void main(String[] args) {
		FixedWaitStrategy strategy = new FixedWaitStrategy();
		for (int i = 0; i < 10; i++) {
			System.out.println("Failure: " + i + ", Wait Seconds: " + strategy.nextSeconds(2, i));
		}
	}
}