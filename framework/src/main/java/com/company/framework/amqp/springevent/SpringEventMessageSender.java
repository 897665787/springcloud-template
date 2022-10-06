package com.company.framework.amqp.springevent;

import java.util.function.Consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.company.common.util.JsonUtil;
import com.company.framework.amqp.MessageSender;
import com.company.framework.amqp.rabbit.constants.HeaderConstants;
import com.company.framework.amqp.springevent.delay.DelayQueueComponent;
import com.company.framework.amqp.springevent.delay.DelayedConsumer;
import com.company.framework.amqp.springevent.event.MessageEvent;

import lombok.extern.slf4j.Slf4j;

/**
 * 只能用于单体服务
 * 
 * @author JQ棣
 */
@Slf4j
@Component
public class SpringEventMessageSender implements MessageSender {

	@Autowired
	private ApplicationContext applicationContext;
	@Autowired
	private DelayQueueComponent delayQueueComponent;

	@Override
	public void sendNormalMessage(String strategyName, Object toJson, String exchange, String routingKey) {
		MessageEvent messageEvent = new MessageEvent();
		if (strategyName != null) {
			messageEvent.setHeader(HeaderConstants.HEADER_STRATEGY_NAME, strategyName);
		}
		messageEvent.setHeader(HeaderConstants.HEADER_PARAMS_CLASS, toJson.getClass().getName());
		String paramsStr = JsonUtil.toJsonString(toJson);
		messageEvent.setJsonStrMsg(paramsStr);
		applicationContext.publishEvent(messageEvent);
		log.info("publishEvent,strategyName:{},toJson:{},exchange:{},routingKey:{}", strategyName, paramsStr, exchange,
				routingKey);
	}

	@Override
	public void sendFanoutMessage(Object toJson, String exchange) {
		sendNormalMessage(null, toJson, exchange, null);
	}

	@Override
	public void sendDelayMessage(String strategyName, Object toJson, String exchange, String routingKey,
			Integer delaySeconds) {
		Consumer<Long> consumer = t -> sendNormalMessage(strategyName, toJson, exchange, routingKey);

		DelayedConsumer delayedConsumer = new DelayedConsumer(consumer, delaySeconds);
		delayQueueComponent.inqueue(delayedConsumer);
	}
}