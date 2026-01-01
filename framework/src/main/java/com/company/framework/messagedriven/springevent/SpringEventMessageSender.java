package com.company.framework.messagedriven.springevent;

import java.util.function.Consumer;

import com.company.framework.messagedriven.constants.HeaderConstants;
import com.company.framework.trace.TraceManager;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import com.company.framework.util.JsonUtil;
import com.company.framework.messagedriven.MessageSender;
import com.company.framework.messagedriven.springevent.delay.DelayQueueComponent;
import com.company.framework.messagedriven.springevent.delay.DelayedConsumer;
import com.company.framework.messagedriven.springevent.event.MessageEvent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 只能用于单体服务
 *
 * @author JQ棣
 */
@Slf4j
@Component
@ConditionalOnProperty(prefix = "template.enable", name = "message-driven", havingValue = "springevent")
@RequiredArgsConstructor
public class SpringEventMessageSender implements MessageSender {

	private final ApplicationEventPublisher applicationEventPublisher;
	private final DelayQueueComponent delayQueueComponent;
	private final TraceManager traceManager;

	@Override
	public void sendNormalMessage(String strategyName, Object toJson, String exchange, String routingKey) {
		MessageEvent messageEvent = new MessageEvent();
		if (strategyName != null) {
			messageEvent.setHeader(HeaderConstants.HEADER_STRATEGY_NAME, strategyName);
		}
		messageEvent.setHeader(HeaderConstants.HEADER_PARAMS_CLASS, toJson.getClass().getName());
		String paramsStr = JsonUtil.toJsonString(toJson);
		messageEvent.setJsonStrMsg(paramsStr);
		messageEvent.setExchange(exchange);
		applicationEventPublisher.publishEvent(messageEvent);
		log.info("publishEvent,strategyName:{},toJson:{},exchange:{},routingKey:{}", strategyName, paramsStr, exchange,
				routingKey);
	}

	@Override
	public void sendBroadcastMessage(Object toJson, String exchange) {
		MessageEvent messageEvent = new MessageEvent();
		messageEvent.setHeader(HeaderConstants.HEADER_PARAMS_CLASS, toJson.getClass().getName());
		String paramsStr = JsonUtil.toJsonString(toJson);
		messageEvent.setJsonStrMsg(paramsStr);
		messageEvent.setExchange(exchange);
		applicationEventPublisher.publishEvent(messageEvent);
		log.info("publishEvent,strategyName:{},toJson:{},exchange:{},routingKey:{}", null, paramsStr, exchange,
				null);
	}

	@Override
	public void sendDelayMessage(String strategyName, Object toJson, String exchange, String routingKey,
			Integer delaySeconds) {
		Consumer<Long> consumer = t -> sendNormalMessage(strategyName, toJson, exchange, routingKey);

		DelayedConsumer delayedConsumer = new DelayedConsumer(consumer, delaySeconds, traceManager.get());
		delayQueueComponent.inqueue(delayedConsumer);
	}
}
