package com.company.order.innercallback;

import org.springframework.stereotype.Component;

import com.company.order.innercallback.strategy.SecondsStrategy;
import com.company.order.innercallback.strategy.SecondsStrategyBeanFactory;

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
