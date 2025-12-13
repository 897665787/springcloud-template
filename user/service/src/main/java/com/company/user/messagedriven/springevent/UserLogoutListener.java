package com.company.user.messagedriven.springevent;

import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.company.framework.messagedriven.constants.BroadcastConstants;
import com.company.framework.messagedriven.constants.HeaderConstants;
import com.company.framework.messagedriven.springevent.event.MessageEvent;
import com.company.framework.messagedriven.springevent.utils.ConsumerUtils;
import com.company.user.messagedriven.strategy.StrategyConstants;

@Component
public class UserLogoutListener {

	@EventListener
	public void userDevice(MessageEvent event) {
		String jsonStrMsg = event.getJsonStrMsg();
		String exchange = event.getExchange();
		if (!BroadcastConstants.USER_LOGOUT.EXCHANGE.equals(exchange)) {
			return;
		}
		Map<String, Object> headers = event.getHeaders();
		String strategyName = StrategyConstants.USERDEVICE_LOGOUT_STRATEGY;
		String paramsClassName = MapUtils.getString(headers, HeaderConstants.HEADER_PARAMS_CLASS);
		ConsumerUtils.handleByStrategy(jsonStrMsg, strategyName, paramsClassName);
	}
}
