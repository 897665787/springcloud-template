package com.company.app.amqp.springevent;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.company.framework.amqp.springevent.event.MessageEvent;
import com.company.framework.amqp.springevent.utils.ConsumerUtils;

@Component
public class SpringEventListener implements ApplicationListener<MessageEvent> {

	@Override
	public void onApplicationEvent(MessageEvent event) {
		String jsonStrMsg = event.getJsonStrMsg();
		ConsumerUtils.handleByStrategy(jsonStrMsg, event);
	}
}
