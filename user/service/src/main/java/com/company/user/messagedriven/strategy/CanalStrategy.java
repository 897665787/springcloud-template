package com.company.user.messagedriven.strategy;

import com.company.framework.messagedriven.BaseStrategy;
import com.company.user.messagedriven.strategy.dto.CanalRow;
import org.springframework.stereotype.Component;

/**
 * canal
 */
@Component(StrategyConstants.CANAL_STRATEGY)
public class CanalStrategy implements BaseStrategy<CanalRow> {

	@Override
	public void doStrategy(CanalRow params) {

	}
}
