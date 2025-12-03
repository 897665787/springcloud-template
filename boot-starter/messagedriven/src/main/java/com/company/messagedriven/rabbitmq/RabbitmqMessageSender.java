package com.company.messagedriven.rabbitmq;

import cn.hutool.core.util.RandomUtil;
import com.company.framework.util.JsonUtil;
import com.company.messagedriven.MessageSender;
import com.company.messagedriven.constants.HeaderConstants;
import com.company.framework.trace.TraceManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Conditional(RabbitMQAutoConfiguration.RabbitMQCondition.class)
public class RabbitmqMessageSender implements MessageSender {

	@Autowired
	private RabbitTemplate rabbitTemplate;
	@Autowired
	private TraceManager traceManager;

	@Override
	public void sendNormalMessage(String strategyName, Object toJson, String exchange, String routingKey) {
		sendMessage(strategyName, toJson, exchange, routingKey, null);
	}

	@Override
	public void sendFanoutMessage(Object toJson, String exchange) {
		sendMessage(null, toJson, exchange, null, null);
	}

	@Override
	public void sendDelayMessage(String strategyName, Object toJson, String exchange, String routingKey,
			Integer delaySeconds) {
		sendMessage(strategyName, toJson, exchange, routingKey, delaySeconds);
	}

	/**
	 * 发送消息
	 *
	 * @param strategyName
	 * @param toJson
	 * @param exchange
	 * @param routingKey
	 * @param delaySeconds
	 */
	private void sendMessage(String strategyName, Object toJson, String exchange, String routingKey,
			Integer delaySeconds) {
		String correlationId = RandomUtil.randomString(32);
		String paramsStr = JsonUtil.toJsonString(toJson);
		rabbitTemplate.convertAndSend(exchange, routingKey, paramsStr, messageToSend -> {
			MessageProperties messageProperties = messageToSend.getMessageProperties();
			if (strategyName != null) {
				messageProperties.setHeader(HeaderConstants.HEADER_STRATEGY_NAME, strategyName);
			}
			messageProperties.setHeader(HeaderConstants.HEADER_PARAMS_CLASS, toJson.getClass().getName());
			messageProperties.setMessageId(traceManager.get());
			if (delaySeconds != null) {
				Integer delayMillis = delaySeconds * 1000;// 设置延时毫秒值
				messageProperties.setDelay(delayMillis);// x-delayed延时
				messageProperties.setExpiration(String.valueOf(delayMillis));// x-dead-letter延时
			}
			return messageToSend;
		}, new CorrelationData(correlationId));
		log.info("convertAndSend,correlationId:{},strategyName:{},toJson:{},exchange:{},routingKey:{},delaySeconds:{}",
				correlationId, strategyName, paramsStr, exchange, routingKey, delaySeconds);
	}
}
