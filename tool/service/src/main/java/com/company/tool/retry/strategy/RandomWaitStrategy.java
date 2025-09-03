package com.company.tool.retry.strategy;

import java.util.Random;

import org.springframework.stereotype.Component;

/**
 * 随机等待策略
 * 
 * @author JQ棣
 */
@Component(WaitStrategyBeanFactory.RANDOM_WAIT_STRATEGY)
public class RandomWaitStrategy implements WaitStrategy {

	private static final Random RANDOM = new Random();

	@Override
	public int nextSeconds(int increaseSeconds, int failure) {
		// 随机范围为 1 到 increaseSeconds
		int min = 1;
		int max = increaseSeconds;
		// 确保max不小于min
		if (max < min) {
			max = min;
		}
		return RANDOM.nextInt(max - min + 1) + min;
	}

	public static void main(String[] args) {
		RandomWaitStrategy strategy = new RandomWaitStrategy();
		for (int i = 0; i < 10; i++) {
			System.out.println("Failure: " + i + ", Wait Seconds: " + strategy.nextSeconds(10, i));
		}
	}
}