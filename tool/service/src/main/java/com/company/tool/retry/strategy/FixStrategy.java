package com.company.tool.retry.strategy;

import org.springframework.stereotype.Component;

/**
 * 固定策略
 * 
 * @author JQ棣
 */
@Component(SecondsStrategyBeanFactory.FIX_SECONDSSTRATEGY)
public class FixStrategy implements SecondsStrategy {

	@Override
	public int nextSeconds(int increaseSeconds, int failure) {
		return increaseSeconds;
	}

}
