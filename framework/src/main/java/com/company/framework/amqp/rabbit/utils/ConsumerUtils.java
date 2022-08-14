package com.company.framework.amqp.rabbit.utils;

import java.io.IOException;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

import org.apache.commons.collections.MapUtils;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;

import com.company.common.exception.BusinessException;
import com.company.common.util.JsonUtil;
import com.company.common.util.MdcUtil;
import com.company.framework.amqp.rabbit.BaseStrategy;
import com.company.framework.amqp.rabbit.constants.HeaderConstants;
import com.company.framework.context.SpringContextUtil;
import com.rabbitmq.client.Channel;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ConsumerUtils {
	private ConsumerUtils() {
	}

	public static <E> void handleByStrategy(String jsonStrMsg, Channel channel, Message message) {
		@SuppressWarnings("unchecked")
		Function<Object, Void> function = entity -> {
			MessageProperties messageProperties = message.getMessageProperties();
			Map<String, Object> headers = messageProperties.getHeaders();
			String strategyName = MapUtils.getString(headers, HeaderConstants.HEADER_STRATEGY_NAME);
			BaseStrategy<E> strategy = SpringContextUtil.getBean(strategyName, BaseStrategy.class);
			strategy.doStrategy((E) entity);
			return null;
		};
		handle(jsonStrMsg, channel, message, function);
	}

	public static <E> void handleByConsumer(String jsonStrMsg, Channel channel, Message message, Consumer<E> consumer) {
		@SuppressWarnings("unchecked")
		Function<Object, Void> function = entity -> {
			consumer.accept((E) entity);
			return null;
		};
		handle(jsonStrMsg, channel, message, function);
	}

	private static void handle(String jsonStrMsg, Channel channel, Message message, Function<Object, Void> function) {
		try {
			if (jsonStrMsg == null) {
				basicAck(channel, message);
				return;
			}
			try {
				MessageProperties messageProperties = message.getMessageProperties();
				MdcUtil.put(messageProperties.getMessageId());
				Map<String, Object> headers = messageProperties.getHeaders();
				String paramsClassName = MapUtils.getString(headers, HeaderConstants.HEADER_PARAMS_CLASS);
				Class<?> paramsClass = null;
				try {
					paramsClass = Class.forName(paramsClassName);
				} catch (ClassNotFoundException e) {
					log.error("ClassNotFoundException", e);
				}
				Object entity = JsonUtil.toEntity(jsonStrMsg, paramsClass);
				function.apply(entity);
			} catch (BusinessException e) {
				// 业务异常一般是校验不通过，可以当做成功处理
				log.warn("BusinessException code:{},message:{}", e.getCode(), e.getMessage());
			} catch (Exception e) {
				log.error("accept error", e);
				throw e;
			}

			basicAck(channel, message);
		} finally {
			MdcUtil.remove();
		}
	}
	
	private static void basicAck(Channel channel, Message message) {
		try {
			channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
		} catch (IOException e) {
			log.error("basicAck error", e);
		}
	}
}
