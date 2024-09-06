package com.company.system.innercallback;

import com.company.system.innercallback.strategy.SecondsStrategy;
import com.company.system.innercallback.strategy.SecondsStrategyBeanFactory;
import org.springframework.stereotype.Component;

/**
 * 增量策略(默认策略)
 *
 * @author JQ棣
 */
@Component(SecondsStrategyBeanFactory.INCREMENT_SECONDSSTRATEGY)
public class IncrementStrategy implements SecondsStrategy {

    @Override
    public int nextSeconds(int increaseSeconds, int failure) {
        return (int) (increaseSeconds * Math.pow(2, failure));
    }

}
