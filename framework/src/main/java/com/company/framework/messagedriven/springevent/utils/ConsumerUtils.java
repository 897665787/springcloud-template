package com.company.framework.messagedriven.springevent.utils;

import java.util.Map;
import java.util.function.Consumer;

import com.company.common.exception.BusinessException;
import com.company.framework.util.JsonUtil;
import com.company.framework.messagedriven.BaseStrategy;
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
	 * @param strategyName
	 * @param paramsClassName
	 */
	public static <E> void handleByStrategy(String jsonStrMsg, String strategyName, String paramsClassName) {
		handleByStrategy(jsonStrMsg, strategyName, paramsClassName, true);
	}

	/**
	 * 使用Strategy处理逻辑
	 *
	 * @param jsonStrMsg
	 * @param strategyName
	 * @param paramsClassName
	 * @param unAckIfException
	 *            如果抛出java.lang.Exception异常则不ack
	 */
	public static <E> void handleByStrategy(String jsonStrMsg, String strategyName, String paramsClassName,
			boolean unAckIfException) {
		@SuppressWarnings("unchecked")
		Consumer<Object> consumer = entity -> {
			BaseStrategy<E> strategy = SpringContextUtil.getBean(strategyName, BaseStrategy.class);
			strategy.doStrategy((E) entity);
		};
		handle(jsonStrMsg, paramsClassName, consumer, unAckIfException);
	}

	/**
	 * 使用自定义Consumer函数处理逻辑
	 *
	 * @param jsonStrMsg
	 * @param paramsClassName
	 * @param consumer
	 */
	public static <E> void handleByConsumer(String jsonStrMsg, String paramsClassName, Consumer<E> consumer) {
		handleByConsumer(jsonStrMsg, paramsClassName, consumer, false);
	}

	/**
	 * 使用自定义Consumer函数处理逻辑
	 *
	 * @param jsonStrMsg
	 * @param paramsClassName
	 * @param consumer
	 * @param unAckIfException
	 *            如果抛出java.lang.Exception异常则不ack
	 */
	public static <E> void handleByConsumer(String jsonStrMsg, String paramsClassName, Consumer<E> consumer,
			boolean unAckIfException) {
		@SuppressWarnings("unchecked")
		Consumer<Object> consumer2 = entity -> {
			consumer.accept((E) entity);
		};
		handle(jsonStrMsg, paramsClassName, consumer2, unAckIfException);
	}

	private static void handle(String jsonStrMsg, String paramsClassName, Consumer<Object> consumer,
			boolean unAckIfException) {
		log.info("jsonStrMsg:{}", jsonStrMsg);
		if (jsonStrMsg == null) {
			return;
		}
		long start = System.currentTimeMillis();
		try {
			Class<?> paramsClass;
			try {
				paramsClass = Class.forName(paramsClassName);
			} catch (ClassNotFoundException e) {
				log.warn("class {} not found,use {} instead", paramsClassName, Map.class.getName());
				paramsClass = Map.class;// 找不到类，就用Map
			}
			Object entity = JsonUtil.toEntity(jsonStrMsg, paramsClass);
			consumer.accept(entity);
		} catch (RuntimeException e) {
			// 业务异常一般是校验不通过，可以当做成功处理
			log.warn("RuntimeException", e);
		} catch (Exception e) {
			log.error("accept error", e);
			if (unAckIfException) {
				return;
			}
		} finally {
			log.info("耗时:{}ms", System.currentTimeMillis() - start);
		}
	}
}
