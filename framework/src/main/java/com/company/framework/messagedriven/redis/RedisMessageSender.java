package com.company.framework.messagedriven.redis;

import cn.hutool.core.util.RandomUtil;
import com.company.framework.messagedriven.MessageSender;
import com.company.framework.messagedriven.constants.HeaderConstants;
import com.company.framework.trace.TraceManager;
import com.company.framework.util.JsonUtil;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Conditional;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Redis消息发送器
 *
 * @author JQ棣
 */
@Slf4j
@Component
@Conditional(RedisAutoConfiguration.RedisCondition.class)
public class RedisMessageSender implements MessageSender {

    private static final String CHANNEL_PREFIX = "mq:channel:";
    private static final String DELAY_QUEUE_PREFIX = "mq:delay:";

    @Autowired
    private RedisTemplate<String, String> redisMessageTemplate;
    @Autowired
    private TraceManager traceManager;

    @Override
    public void sendNormalMessage(String strategyName, Object toJson, String channel, String routingKey) {
        sendMessage(strategyName, toJson, channel, routingKey, null);
    }

    @Override
    public void sendFanoutMessage(Object toJson, String channel) {
        // Redis 使用发布订阅模式实现广播
        sendMessage(null, toJson, channel, null, null);
    }

    @Override
    public void sendDelayMessage(String strategyName, Object toJson, String channel, String routingKey,
                                  Integer delaySeconds) {
        sendMessage(strategyName, toJson, channel, routingKey, delaySeconds);
    }

    /**
     * 发送消息
     *
     * @param strategyName 策略名称
     * @param toJson       消息内容
     * @param channel      频道（对应exchange）
     * @param routingKey   路由键（对应队列key）
     * @param delaySeconds 延时秒数
     */
    private void sendMessage(String strategyName, Object toJson, String channel, String routingKey,
                             Integer delaySeconds) {
        String correlationId = RandomUtil.randomString(32);
        String paramsStr = JsonUtil.toJsonString(toJson);

        // 构建消息头
        Map<String, String> headers = Maps.newHashMap();
        if (strategyName != null) {
            headers.put(HeaderConstants.HEADER_STRATEGY_NAME, strategyName);
        }
        headers.put(HeaderConstants.HEADER_PARAMS_CLASS, toJson.getClass().getName());
        headers.put(HeaderConstants.HEADER_MESSAGE_ID, traceManager.get());
        headers.put("correlation_id", correlationId);

        // 构建完整消息
        Map<String, Object> message = Maps.newHashMap();
        message.put("headers", headers);
        message.put("body", paramsStr);
        if (routingKey != null) {
            message.put("routing_key", routingKey);
        }

        String messageJson = JsonUtil.toJsonString(message);

        if (delaySeconds != null && delaySeconds > 0) {
            // 延时消息：存储到 sorted set，使用时间戳作为score
            long executeTime = System.currentTimeMillis() + delaySeconds * 1000L;
            String delayQueueKey = DELAY_QUEUE_PREFIX + channel;
            redisMessageTemplate.opsForZSet().add(delayQueueKey, messageJson, executeTime);
            log.info("sendDelayMessage,correlationId:{},strategyName:{},toJson:{},channel:{},routingKey:{},delaySeconds:{}",
                    correlationId, strategyName, paramsStr, channel, routingKey, delaySeconds);
        } else {
            // 普通消息：使用发布订阅
            String channelKey = CHANNEL_PREFIX + channel;
            redisMessageTemplate.convertAndSend(channelKey, messageJson);
            log.info("sendNormalMessage,correlationId:{},strategyName:{},toJson:{},channel:{},routingKey:{}",
                    correlationId, strategyName, paramsStr, channel, routingKey);
        }
    }
}
