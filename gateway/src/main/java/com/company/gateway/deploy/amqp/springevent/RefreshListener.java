package com.company.gateway.deploy.amqp.springevent;

import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.company.gateway.amqp.rabbit.constants.FanoutConstants;
import com.company.gateway.amqp.rabbit.constants.HeaderConstants;
import com.company.gateway.amqp.springevent.event.MessageEvent;
import com.company.gateway.amqp.springevent.utils.ConsumerUtils;
import com.company.gateway.deploy.amqp.strategy.StrategyConstants;


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
