package com.company.order.retry.strategy;

/**
 * 增量策略(默认策略)
 * 
 * @author Candi
 */
public class IncrementStrategy implements SecondsStrategy {

	@Override
	public int nextSeconds(int increaseSeconds, int failure) {
		return (int) (increaseSeconds * Math.pow(2, failure));
	}

}
