package com.company.gateway.deploy.amqp.strategy;

import com.company.gateway.amqp.BaseStrategy;
import com.company.gateway.deploy.RefreshHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

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
