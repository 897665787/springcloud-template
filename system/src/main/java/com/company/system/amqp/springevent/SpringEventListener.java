package com.company.system.amqp.springevent;

import java.util.Map;

import com.company.framework.amqp.constants.HeaderConstants;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.company.framework.amqp.springevent.event.MessageEvent;
import com.company.framework.amqp.springevent.utils.ConsumerUtils;

@Component
public class SpringEventListener implements ApplicationListener<MessageEvent> {

	@Override
	public void onApplicationEvent(MessageEvent event) {
		String jsonStrMsg = event.getJsonStrMsg();
		Map<String, Object> headers = event.getHeaders();
		String strategyName = MapUtils.getString(headers, HeaderConstants.HEADER_STRATEGY_NAME);
		if (StringUtils.isBlank(strategyName)) {
			return;
		}
		String paramsClassName = MapUtils.getString(headers, HeaderConstants.HEADER_PARAMS_CLASS);
		ConsumerUtils.handleByStrategy(jsonStrMsg, strategyName, paramsClassName);
	}
}
