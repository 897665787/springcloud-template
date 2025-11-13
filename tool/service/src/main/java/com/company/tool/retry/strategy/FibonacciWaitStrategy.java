package com.company.tool.retry.strategy;

import org.springframework.stereotype.Component;

/**
 * 斐波那契数列策略
 * 
 * @author JQ棣
 */
@Component(WaitStrategyBeanFactory.FIBONACCI_WAIT_STRATEGY)
public class FibonacciWaitStrategy implements WaitStrategy {

	@Override
	public int nextSeconds(int increaseSeconds, int failure) {
		if (failure <= 0) {
			return increaseSeconds;
		}

		int prev = 0;
		int curr = 1;
		for (int i = 1; i < failure; i++) {
			int temp = curr;
			curr = prev + curr;
			prev = temp;
		}
		
		return increaseSeconds * curr;
	}

	public static void main(String[] args) {
		FibonacciWaitStrategy fibonacciWaitStrategy = new FibonacciWaitStrategy();
		for (int i = 0; i < 10; i++) {
			System.out.println("Failure: " + i + ", Wait Seconds: " + fibonacciWaitStrategy.nextSeconds(2, i));
		}
	}
}