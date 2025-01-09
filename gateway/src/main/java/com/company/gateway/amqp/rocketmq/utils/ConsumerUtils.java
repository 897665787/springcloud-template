package com.company.gateway.amqp.rocketmq.utils;

import com.company.common.exception.BusinessException;
import com.company.common.util.JsonUtil;
import com.company.common.util.MdcUtil;
import com.company.gateway.amqp.BaseStrategy;
import com.company.gateway.amqp.constants.HeaderConstants;
import com.company.gateway.context.SpringContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;

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
	 * @param properties
	 */
	public static <E> void handleByStrategy(String jsonStrMsg, Map<String, String> properties) {
		handleByStrategy(jsonStrMsg, properties, true);
	}

	/**
	 * 使用Strategy处理逻辑
	 *
	 * @param jsonStrMsg
	 * @param properties
	 * @param unAckIfException
	 *            如果抛出java.lang.Exception异常则不ack
	 */
	public static <E> void handleByStrategy(String jsonStrMsg, Map<String, String> properties,
											boolean unAckIfException) {
		@SuppressWarnings("unchecked")
		Consumer<Object> consumer = entity -> {
			String strategyName = MapUtils.getString(properties, HeaderConstants.HEADER_STRATEGY_NAME);
			BaseStrategy<E> strategy = SpringContextUtil.getBean(strategyName, BaseStrategy.class);
			strategy.doStrategy((E) entity);
		};
		handle(jsonStrMsg, properties, consumer, unAckIfException);
	}

	/**
	 * 使用自定义Consumer函数处理逻辑
	 *
	 * @param jsonStrMsg
	 * @param properties
	 * @param consumer
	 */
	public static <E> void handleByConsumer(String jsonStrMsg, Map<String, String> properties, Consumer<E> consumer) {
		handleByConsumer(jsonStrMsg, properties, consumer, false);
	}

	/**
	 * 使用自定义Consumer函数处理逻辑
	 *
	 * @param jsonStrMsg
	 * @param properties
	 * @param consumer
	 * @param unAckIfException
	 *            如果抛出java.lang.Exception异常则不ack
	 */
	public static <E> void handleByConsumer(String jsonStrMsg, Map<String, String> properties, Consumer<E> consumer,
											boolean unAckIfException) {
		@SuppressWarnings("unchecked")
		Consumer<Object> consumer2 = entity -> {
			consumer.accept((E) entity);
		};
		handle(jsonStrMsg, properties, consumer2, unAckIfException);
	}

	private static void handle(String jsonStrMsg, Map<String, String> properties, Consumer<Object> consumer,
							   boolean unAckIfException) {
		try {
			MdcUtil.put(properties.get(HeaderConstants.HEADER_MESSAGE_ID));
			if (jsonStrMsg == null) {
				log.info("jsonStrMsg is null,properties:{}", JsonUtil.toJsonString(properties));
				return;
			}
			long start = System.currentTimeMillis();
			try {
				log.info("jsonStrMsg:{},properties:{}", jsonStrMsg, JsonUtil.toJsonString(properties));
				String paramsClassName = MapUtils.getString(properties, HeaderConstants.HEADER_PARAMS_CLASS);
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
			MdcUtil.remove();
		}
	}
}