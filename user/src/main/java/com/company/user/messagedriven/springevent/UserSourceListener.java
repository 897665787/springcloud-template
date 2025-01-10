package com.company.user.messagedriven.springevent;

import java.util.Map;

import com.company.framework.messagedriven.constants.HeaderConstants;
import org.apache.commons.collections4.MapUtils;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.company.framework.messagedriven.constants.FanoutConstants;
import com.company.framework.messagedriven.springevent.event.MessageEvent;
import com.company.framework.messagedriven.springevent.utils.ConsumerUtils;
import com.company.user.messagedriven.strategy.StrategyConstants;

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
