package com.company.tool.innercallback;

import org.springframework.stereotype.Component;

import com.company.tool.innercallback.strategy.SecondsStrategy;
import com.company.tool.innercallback.strategy.SecondsStrategyBeanFactory;

/**
 * 固定策略
 * 
 * @author Candi
 */
@Component(SecondsStrategyBeanFactory.FIX_SECONDSSTRATEGY)
public class FixStrategy implements SecondsStrategy {

	@Override
	public int nextSeconds(int increaseSeconds, int failure) {
		return increaseSeconds;
	}

}
