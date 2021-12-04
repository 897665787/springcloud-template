package com.company.framework.amqp.no;

import org.springframework.stereotype.Component;

import com.company.framework.amqp.MessageSender;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class NoMessageSender implements MessageSender {

	@Override
	public void sendNormalMessage(String strategyName, Object toJson, String exchange, String routingKey) {
		log.warn("no message sender");
	}

	@Override
	public void sendFanoutMessage(Object toJson, String exchange) {
		log.warn("no message sender");
	}

	@Override
	public void sendDelayMessage(String strategyName, Object toJson, String exchange, String routingKey,
			Integer delaySeconds) {
		log.warn("no message sender");
	}
}