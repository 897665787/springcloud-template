package com.company.zuul.deploy.amqp.springevent;

import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.company.zuul.amqp.constants.FanoutConstants;
import com.company.zuul.amqp.constants.HeaderConstants;
import com.company.zuul.amqp.springevent.event.MessageEvent;
import com.company.zuul.amqp.springevent.utils.ConsumerUtils;
import com.company.zuul.deploy.amqp.strategy.StrategyConstants;


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
