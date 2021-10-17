package com.company.framework.amqp.no;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.company.framework.amqp.MessageSender;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class NoMessageSender implements MessageSender {

	public void sendMessage(Map<String, Object> params, String exchange, String routingKey) {
		log.warn("no message sender");
	}

	@Override
	public void sendMessage(Map<String, Object> params, String exchange, String routingKey, Integer delaySeconds) {
		log.warn("no message sender");
	}
}