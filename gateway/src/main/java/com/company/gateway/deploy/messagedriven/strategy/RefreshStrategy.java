package com.company.gateway.deploy.messagedriven.strategy;

import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.company.gateway.deploy.RefreshHandler;
import com.company.gateway.messagedriven.BaseStrategy;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component(StrategyConstants.REFRESH_STRATEGY)
public class RefreshStrategy implements BaseStrategy<Map<String, Object>> {
	@Autowired
	private RefreshHandler refreshHandler;

	@Override
	public void doStrategy(Map<String, Object> params) {
		String application = MapUtils.getString(params, "application");
		refreshHandler.refresh(application);
		log.info("#### refresh success");
	}
}
