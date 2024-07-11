package com.company.order.retry.strategy;

/**
 * 固定策略
 * 
 * @author Candi
 */
public class FixStrategy implements SecondsStrategy {

	@Override
	public int nextSeconds(int increaseSeconds, int failure) {
		return increaseSeconds;
	}

}
