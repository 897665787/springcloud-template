package com.company.framework.amqp.springevent.utils;

import java.util.Map;
import java.util.function.Consumer;

import org.apache.commons.collections.MapUtils;

import com.company.common.exception.BusinessException;
import com.company.common.util.JsonUtil;
import com.company.framework.amqp.BaseStrategy;
import com.company.framework.amqp.rabbit.constants.HeaderConstants;
import com.company.framework.amqp.springevent.event.MessageEvent;
import com.company.framework.context.SpringContextUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ConsumerUtils {
	private ConsumerUtils() {
	}

	/**
	 * 使用Strategy处理逻辑
	 * 
	 * @param jsonStrMsg
	 * @param event
	 */
	public static <E> void handleByStrategy(String jsonStrMsg, MessageEvent event) {
		handleByStrategy(jsonStrMsg, event, true);
	}

	/**
	 * 使用Strategy处理逻辑
	 * 
	 * @param jsonStrMsg
	 * @param event
	 * @param unAckIfException
	 *            如果抛出java.lang.Exception异常则不ack
	 */
	public static <E> void handleByStrategy(String jsonStrMsg, MessageEvent event,
			boolean unAckIfException) {
		@SuppressWarnings("unchecked")
		Consumer<Object> consumer = entity -> {
			Map<String, Object> headers = event.getHeaders();
			String strategyName = MapUtils.getString(headers, HeaderConstants.HEADER_STRATEGY_NAME);
			BaseStrategy<E> strategy = SpringContextUtil.getBean(strategyName, BaseStrategy.class);
			strategy.doStrategy((E) entity);
		};
		handle(jsonStrMsg, event, consumer, unAckIfException);
	}

	/**
	 * 使用自定义Consumer函数处理逻辑
	 * 
	 * @param jsonStrMsg
	 * @param event
	 * @param consumer
	 */
	public static <E> void handleByConsumer(String jsonStrMsg, MessageEvent event, Consumer<E> consumer) {
		handleByConsumer(jsonStrMsg, event, consumer, false);
	}

	/**
	 * 使用自定义Consumer函数处理逻辑
	 * 
	 * @param jsonStrMsg
	 * @param event
	 * @param consumer
	 * @param unAckIfException
	 *            如果抛出java.lang.Exception异常则不ack
	 */
	public static <E> void handleByConsumer(String jsonStrMsg, MessageEvent event, Consumer<E> consumer,
			boolean unAckIfException) {
		@SuppressWarnings("unchecked")
		Consumer<Object> consumer2 = entity -> {
			consumer.accept((E) entity);
		};
		handle(jsonStrMsg, event, consumer2, unAckIfException);
	}

	private static void handle(String jsonStrMsg, MessageEvent event, Consumer<Object> consumer,
			boolean unAckIfException) {
		try {
			if (jsonStrMsg == null) {
				log.info("jsonStrMsg is null");
				return;
			}
			long start = System.currentTimeMillis();
			try {
				log.info("jsonStrMsg:{}", jsonStrMsg);
				Map<String, Object> headers = event.getHeaders();
				String paramsClassName = MapUtils.getString(headers, HeaderConstants.HEADER_PARAMS_CLASS);
				Class<?> paramsClass = null;
				try {
					paramsClass = Class.forName(paramsClassName);
				} catch (ClassNotFoundException e) {
					log.error("ClassNotFoundException", e);
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
		} finally {
		}
	}
}
