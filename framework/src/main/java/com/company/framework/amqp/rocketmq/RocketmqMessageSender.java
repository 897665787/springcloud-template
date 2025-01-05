package com.company.framework.amqp.rocketmq;

import cn.hutool.core.util.RandomUtil;
import com.company.common.util.JsonUtil;
import com.company.common.util.MdcUtil;
import com.company.framework.amqp.MessageSender;
import com.company.framework.amqp.rabbit.constants.HeaderConstants;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Primary
@Component
//@Conditional(RabbitCondition.class)
public class RocketmqMessageSender implements MessageSender {

//	@Autowired(required = false)
	private RocketMQTemplate rocketMQTemplate;

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
		if (rocketMQTemplate == null) {
			log.warn("rocketMQTemplate not init");
			return;
		}

		String correlationId = RandomUtil.randomString(32);
		String paramsStr = JsonUtil.toJsonString(toJson);

		Map<String, Object> headers = Maps.newHashMap();
		headers.put(HeaderConstants.HEADER_STRATEGY_NAME, strategyName);
		headers.put(HeaderConstants.HEADER_PARAMS_CLASS, toJson.getClass().getName());
		headers.put("message_id", MdcUtil.get());

		MessageHeaders messageHeaders = new MessageHeaders(headers);
		Message<String> message = MessageBuilder.createMessage(paramsStr, messageHeaders);

		SendCallback sendCallback = new SendCallback() {
			@Override
			public void onSuccess(SendResult sendResult) {
				log.info("onSuccess,sendResult:{}", JsonUtil.toJsonString(sendResult));
			}

			@Override
			public void onException(Throwable e) {
				log.error("onException", e);
			}
		};
//		rocketMQTemplate.asyncSend(exchange, "", sendCallback);
//		rocketMQTemplate.asyncSend(exchange, message, sendCallback);
		rocketMQTemplate.asyncSend(exchange, message, sendCallback,
				rocketMQTemplate.getProducer().getSendMsgTimeout(), 0);

//		rocketMQTemplate.convertAndSend(exchange, routingKey, paramsStr, messageToSend -> {
//			MessageProperties messageProperties = messageToSend.getMessageProperties();
//			if (strategyName != null) {
//				messageProperties.setHeader(HeaderConstants.HEADER_STRATEGY_NAME, strategyName);
//			}
//			messageProperties.setHeader(HeaderConstants.HEADER_PARAMS_CLASS, toJson.getClass().getName());
//			messageProperties.setMessageId(MdcUtil.get());
//			if (delaySeconds != null) {
//				Integer delayMillis = delaySeconds * 1000;// 设置延时毫秒值
//				messageProperties.setDelay(delayMillis);// x-delayed延时
//				messageProperties.setExpiration(String.valueOf(delayMillis));// x-dead-letter延时
//			}
//			return messageToSend;
//		}, new CorrelationData(correlationId));
		log.info("convertAndSend,correlationId:{},strategyName:{},toJson:{},exchange:{},routingKey:{},delaySeconds:{}",
				correlationId, strategyName, paramsStr, exchange, routingKey, delaySeconds);
	}
}