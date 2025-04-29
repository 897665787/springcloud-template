package com.company.framework.messagedriven.redis;

import com.company.common.util.JsonUtil;
import com.company.framework.autoconfigure.RedisAutoConfiguration;
import com.company.framework.messagedriven.MessageSender;
import com.company.framework.messagedriven.constants.HeaderConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Conditional;
import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.data.redis.connection.stream.StreamRecords;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Conditional(RedisAutoConfiguration.RedisCondition.class)
public class RedisMessageSender implements MessageSender {

	@Autowired
	private StringRedisTemplate stringRedisTemplate;

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

		MessageStreamKey streamKey = new MessageStreamKey();
		if (strategyName != null) {
			streamKey.setHeader(HeaderConstants.HEADER_STRATEGY_NAME, strategyName);
		}
		streamKey.setHeader(HeaderConstants.HEADER_PARAMS_CLASS, toJson.getClass().getName());
		String paramsStr = JsonUtil.toJsonString(toJson);
		streamKey.setJsonStrMsg(paramsStr);
		streamKey.setExchange(exchange);

		ObjectRecord<String, String> record = StreamRecords.newRecord().ofObject(paramsStr).withStreamKey(exchange);
		stringRedisTemplate.opsForStream().add(record);
		log.info("sendMessage,strategyName:{},toJson:{},exchange:{},routingKey:{}", strategyName, paramsStr, exchange,
				routingKey);
	}
}