package com.company.framework.messagedriven.redis;

import com.company.framework.messagedriven.MessageSender;
import com.company.framework.messagedriven.constants.HeaderConstants;
import com.company.framework.messagedriven.redis.delay.DelayQueueComponent;
import com.company.framework.trace.TraceManager;
import com.company.framework.util.JsonUtil;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Conditional;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Redis消息发送器
 *
 * @author JQ棣
 */
@Slf4j
@Component
@Conditional(RedisAutoConfiguration.RedisCondition.class)
public class RedisMessageSender implements MessageSender {

    @Autowired
    @Qualifier("mqStringRedisTemplate")
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private DelayQueueComponent delayQueueComponent;
    @Autowired
    private TraceManager traceManager;

    @Override
    public void sendNormalMessage(String strategyName, Object toJson, String exchange, String routingKey) {
        sendMessage(strategyName, toJson, exchange, routingKey, null);
    }

    @Override
    public void sendFanoutMessage(Object toJson, String exchange) {
        // Redis 使用发布订阅模式实现广播
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
     * @param strategyName 策略名称
     * @param toJson       消息内容
     * @param exchange      频道（对应exchange）
     * @param routingKey   路由键（对应队列key）
     * @param delaySeconds 延时秒数
     */
    private void sendMessage(String strategyName, Object toJson, String exchange, String routingKey,
                             Integer delaySeconds) {
        String paramsStr = JsonUtil.toJsonString(toJson);

        // 构建完整消息
        Map<String, Object> message = Maps.newHashMap();
        if (strategyName != null) {
            message.put(HeaderConstants.HEADER_STRATEGY_NAME, strategyName);
        }
        message.put(HeaderConstants.HEADER_PARAMS_CLASS, toJson.getClass().getName());
        message.put(HeaderConstants.HEADER_MESSAGE_ID, traceManager.get());
        message.put("body", paramsStr);

        String messageJson = JsonUtil.toJsonString(message);
        String channel = exchange;
        if (routingKey != null) {
            channel = String.format("%s:%s", exchange, routingKey);
        }
        if (delaySeconds != null && delaySeconds > 0) {
            delayQueueComponent.inqueue(exchange, routingKey, messageJson, delaySeconds);
            log.info("inqueue,strategyName:{},toJson:{},exchange:{},routingKey:{},delaySeconds:{}", strategyName, paramsStr, exchange, routingKey, delaySeconds);
        } else {
            // 普通消息：使用发布订阅
            stringRedisTemplate.convertAndSend(channel, messageJson);
            log.info("convertAndSend,channel:{},messageJson:{}", channel, messageJson);
        }
    }
}
