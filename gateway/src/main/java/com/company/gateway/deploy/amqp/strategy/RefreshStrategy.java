package com.company.gateway.deploy.amqp.strategy;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.company.gateway.amqp.BaseStrategy;
import com.company.gateway.deploy.RefreshHandler;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component(StrategyConstants.REFRESH_STRATEGY)
public class RefreshStrategy implements BaseStrategy<Map<String, Object>> {
	@Autowired
	private RefreshHandler refreshHandler;

	@Override
	public void doStrategy(Map<String, Object> params) {
		refreshHandler.refresh();
		log.info("#### refresh success");
	}
}
