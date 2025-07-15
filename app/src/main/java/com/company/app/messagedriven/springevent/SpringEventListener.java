package com.company.app.messagedriven.springevent;

import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.company.framework.messagedriven.constants.HeaderConstants;
import com.company.framework.messagedriven.springevent.event.MessageEvent;
import com.company.framework.messagedriven.springevent.utils.ConsumerUtils;

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
