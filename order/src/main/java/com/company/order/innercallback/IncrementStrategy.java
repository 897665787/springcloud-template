package com.company.order.innercallback;

import org.springframework.stereotype.Component;

import com.company.order.innercallback.strategy.SecondsStrategy;
import com.company.order.innercallback.strategy.SecondsStrategyBeanFactory;

/**
 * 增量策略(默认策略)
 * 
 * @author Candi
 */
@Component(SecondsStrategyBeanFactory.INCREMENT_SECONDSSTRATEGY)
public class IncrementStrategy implements SecondsStrategy {

	@Override
	public int nextSeconds(int increaseSeconds, int failure) {
		return (int) (increaseSeconds * Math.pow(2, failure));
	}

}
