package com.company.gateway.messagedriven.rabbitmq.utils;

import com.company.common.exception.BusinessException;
import com.company.common.util.JsonUtil;
import com.company.gateway.context.SpringContextUtil;
import com.company.gateway.messagedriven.BaseStrategy;
import com.company.gateway.messagedriven.constants.HeaderConstants;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;

import java.io.IOException;
import java.util.Map;
import java.util.function.Consumer;

@Slf4j
public class ConsumerUtils {
	private ConsumerUtils() {
	}

	/**
	 * 使用Strategy处理逻辑
	 *
	 * @param jsonStrMsg
	 * @param channel
	 * @param message
	 */
	public static <E> void handleByStrategy(String jsonStrMsg, Channel channel, Message message) {
		handleByStrategy(jsonStrMsg, channel, message, true);
	}

	/**
	 * 使用Strategy处理逻辑
	 *
	 * @param jsonStrMsg
	 * @param channel
	 * @param message
	 * @param unAckIfException
	 *            如果抛出java.lang.Exception异常则不ack
	 */
	public static <E> void handleByStrategy(String jsonStrMsg, Channel channel, Message message,
											boolean unAckIfException) {
		@SuppressWarnings("unchecked")
		Consumer<Object> consumer = entity -> {
			MessageProperties messageProperties = message.getMessageProperties();
			Map<String, Object> headers = messageProperties.getHeaders();
			String strategyName = MapUtils.getString(headers, HeaderConstants.HEADER_STRATEGY_NAME);
			BaseStrategy<E> strategy = SpringContextUtil.getBean(strategyName, BaseStrategy.class);
			strategy.doStrategy((E) entity);
		};
		handle(jsonStrMsg, channel, message, consumer, unAckIfException);
	}

	/**
	 * 使用自定义Consumer函数处理逻辑
	 *
	 * @param jsonStrMsg
	 * @param channel
	 * @param message
	 * @param consumer
	 */
	public static <E> void handleByConsumer(String jsonStrMsg, Channel channel, Message message, Consumer<E> consumer) {
		handleByConsumer(jsonStrMsg, channel, message, consumer, false);
	}

	/**
	 * 使用自定义Consumer函数处理逻辑
	 *
	 * @param jsonStrMsg
	 * @param channel
	 * @param message
	 * @param consumer
	 * @param unAckIfException
	 *            如果抛出java.lang.Exception异常则不ack
	 */
	public static <E> void handleByConsumer(String jsonStrMsg, Channel channel, Message message, Consumer<E> consumer,
											boolean unAckIfException) {
		@SuppressWarnings("unchecked")
		Consumer<Object> consumer2 = entity -> {
			consumer.accept((E) entity);
		};
		handle(jsonStrMsg, channel, message, consumer2, unAckIfException);
	}

	private static void handle(String jsonStrMsg, Channel channel, Message message, Consumer<Object> consumer,
							   boolean unAckIfException) {
		log.info("jsonStrMsg:{}", jsonStrMsg);
		if (jsonStrMsg == null) {
			basicAck(channel, message);
			return;
		}
		long start = System.currentTimeMillis();
		try {
			MessageProperties messageProperties = message.getMessageProperties();
			Map<String, Object> headers = messageProperties.getHeaders();
			String paramsClassName = MapUtils.getString(headers, HeaderConstants.HEADER_PARAMS_CLASS);
			Class<?> paramsClass;
			try {
				paramsClass = Class.forName(paramsClassName);
			} catch (ClassNotFoundException e) {
				log.warn("class {} not found,use {} instead", paramsClassName, Map.class.getName());
				paramsClass = Map.class;// 找不到类，就用Map
			}
			Object entity = JsonUtil.toEntity(jsonStrMsg, paramsClass);
			consumer.accept(entity);
		} catch (BusinessException e) {
			// 业务异常一般是校验不通过，可以当做成功处理
			log.warn("BusinessException code:{},message:{}", e.getCode(), e.getMessage());
		} catch (Exception e) {
			log.error("accept error", e);
			if (unAckIfException) {
				return;
			}
		} finally {
			log.info("耗时:{}ms", System.currentTimeMillis() - start);
		}
		basicAck(channel, message);
	}

	private static void basicAck(Channel channel, Message message) {
		try {
			channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
		} catch (IOException e) {
			log.error("basicAck error", e);
		}
	}
}
