package com.company.tool.retry.strategy;

import org.springframework.stereotype.Component;

/**
 * 指数退避策略
 * 
 * @author JQ棣
 */
@Component(WaitStrategyBeanFactory.EXPONENTIAL_WAIT_STRATEGY)
public class ExponentialWaitStrategy implements WaitStrategy {

	@Override
	public int nextSeconds(int increaseSeconds, int failure) {
		return (int) (increaseSeconds * Math.pow(2, failure));
	}

	public static void main(String[] args) {
		ExponentialWaitStrategy strategy = new ExponentialWaitStrategy();
		for (int i = 0; i < 10; i++) {
			System.out.println("Failure: " + i + ", Wait Seconds: " + strategy.nextSeconds(2, i));
		}
	}
}