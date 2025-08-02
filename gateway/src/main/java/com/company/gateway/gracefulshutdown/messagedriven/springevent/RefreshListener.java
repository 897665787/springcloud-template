package com.company.gateway.gracefulshutdown.messagedriven.springevent;

import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.company.gateway.gracefulshutdown.messagedriven.strategy.StrategyConstants;
import com.company.gateway.messagedriven.constants.FanoutConstants;
import com.company.gateway.messagedriven.constants.HeaderConstants;
import com.company.gateway.messagedriven.springevent.event.MessageEvent;
import com.company.gateway.messagedriven.springevent.utils.ConsumerUtils;


@Component
public class RefreshListener {

	@EventListener
	public void refresh(MessageEvent event) {
		String jsonStrMsg = event.getJsonStrMsg();
		String exchange = event.getExchange();
		if (!FanoutConstants.DEPLOY.EXCHANGE.equals(exchange)) {
			return;
		}
		Map<String, Object> headers = event.getHeaders();
		String strategyName = StrategyConstants.REFRESH_STRATEGY;
		String paramsClassName = MapUtils.getString(headers, HeaderConstants.HEADER_PARAMS_CLASS);
		ConsumerUtils.handleByStrategy(jsonStrMsg, strategyName, paramsClassName);
	}
}
