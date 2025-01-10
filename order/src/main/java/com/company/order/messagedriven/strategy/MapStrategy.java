package com.company.order.messagedriven.strategy;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.company.framework.messagedriven.BaseStrategy;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component(StrategyConstants.MAP_STRATEGY)
public class MapStrategy implements BaseStrategy<Map<String, Object>> {

	@Override
	public void doStrategy(Map<String, Object> e) {
		log.info("mapStrategy:{}", e);
	}
}
