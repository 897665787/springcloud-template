package com.company.user.amqp.springevent;

import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.company.framework.amqp.rabbit.constants.FanoutConstants;
import com.company.framework.amqp.rabbit.constants.HeaderConstants;
import com.company.framework.amqp.springevent.event.MessageEvent;
import com.company.framework.amqp.springevent.utils.ConsumerUtils;
import com.company.user.amqp.strategy.StrategyConstants;

@Component
public class UserSourceListener {

	@EventListener
	public void loginRecord(MessageEvent event) {
		String jsonStrMsg = event.getJsonStrMsg();
		String exchange = event.getExchange();
		if (!FanoutConstants.USER_SOURCE.EXCHANGE.equals(exchange)) {
			return;
		}
		Map<String, Object> headers = event.getHeaders();
		String strategyName = StrategyConstants.SOURCERECORD_STRATEGY;
		String paramsClassName = MapUtils.getString(headers, HeaderConstants.HEADER_PARAMS_CLASS);
		ConsumerUtils.handleByStrategy(jsonStrMsg, strategyName, paramsClassName);
	}
}
