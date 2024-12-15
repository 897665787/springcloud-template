package com.company.system.innercallback;

import com.company.system.innercallback.strategy.SecondsStrategy;
import com.company.system.innercallback.strategy.SecondsStrategyBeanFactory;
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
