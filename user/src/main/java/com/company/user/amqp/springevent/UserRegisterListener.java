package com.company.user.amqp.springevent;

import java.util.Map;

import com.company.framework.amqp.constants.HeaderConstants;
import org.apache.commons.collections4.MapUtils;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.company.framework.amqp.constants.FanoutConstants;
import com.company.framework.amqp.springevent.event.MessageEvent;
import com.company.framework.amqp.springevent.utils.ConsumerUtils;
import com.company.user.amqp.strategy.StrategyConstants;

@Component
public class UserRegisterListener {

	@EventListener
	public void bindEmail(MessageEvent event) {
		String jsonStrMsg = event.getJsonStrMsg();
		String exchange = event.getExchange();
		if (!FanoutConstants.USER_REGISTER.EXCHANGE.equals(exchange)) {
			return;
		}
		Map<String, Object> headers = event.getHeaders();
		String strategyName = StrategyConstants.BINDEMAIL_STRATEGY;
		String paramsClassName = MapUtils.getString(headers, HeaderConstants.HEADER_PARAMS_CLASS);
		ConsumerUtils.handleByStrategy(jsonStrMsg, strategyName, paramsClassName);
	}

	@EventListener
	public void bindMobile(MessageEvent event) {
		String jsonStrMsg = event.getJsonStrMsg();
		String exchange = event.getExchange();
		if (!FanoutConstants.USER_REGISTER.EXCHANGE.equals(exchange)) {
			return;
		}
		Map<String, Object> headers = event.getHeaders();
		String strategyName = StrategyConstants.BINDMOBILE_STRATEGY;
		String paramsClassName = MapUtils.getString(headers, HeaderConstants.HEADER_PARAMS_CLASS);
		ConsumerUtils.handleByStrategy(jsonStrMsg, strategyName, paramsClassName);
	}
}
