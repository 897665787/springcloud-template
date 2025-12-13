package com.company.web.messagedriven.redis.consumer;

import com.company.framework.messagedriven.constants.BroadcastConstants;
import com.company.framework.messagedriven.constants.HeaderConstants;
import com.company.framework.messagedriven.redis.RedisMQAutoConfiguration;
import com.company.framework.messagedriven.redis.utils.ConsumerUtils;
import com.company.framework.util.JsonUtil;
import com.company.web.messagedriven.strategy.StrategyConstants;
import org.apache.commons.collections.MapUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Map;

@Component
@Conditional(RedisMQAutoConfiguration.RedisMQCondition.class)
public class OrderCreateConsumer {

    @Bean
    public MessageListener smsOrderCreateMessageListener() {
        return new MessageListener() {
            @Override
            public void onMessage(Message message, byte[] pattern) {
                String messageBody = new String(message.getBody(), StandardCharsets.UTF_8);
                @SuppressWarnings("unchecked")
                Map<String, Object> messageMap = JsonUtil.toEntity(messageBody, Map.class);

                String body = MapUtils.getString(messageMap, "body");
                String strategyName = StrategyConstants.ORDERCREATE_SMS_STRATEGY;
                String paramsClass = MapUtils.getString(messageMap, HeaderConstants.HEADER_PARAMS_CLASS);
                ConsumerUtils.handleByStrategy(body, strategyName, paramsClass);
            }
        };
    }

    @Bean
    public Object smsOrderCreateConsumer(RedisMessageListenerContainer container, MessageListener smsOrderCreateMessageListener) {
        String channel = BroadcastConstants.ORDER_CREATE.EXCHANGE;
        container.addMessageListener(smsOrderCreateMessageListener, new ChannelTopic(channel));
        return new Object();
    }

    @Bean
    public MessageListener countmoneyOrderCreateMessageListener() {
        return new MessageListener() {
            @Override
            public void onMessage(Message message, byte[] pattern) {
                String messageBody = new String(message.getBody(), StandardCharsets.UTF_8);
                @SuppressWarnings("unchecked")
                Map<String, Object> messageMap = JsonUtil.toEntity(messageBody, Map.class);

                String body = MapUtils.getString(messageMap, "body");
                String strategyName = StrategyConstants.ORDERCREATE_COUNTMONEY_STRATEGY;
                String paramsClass = MapUtils.getString(messageMap, HeaderConstants.HEADER_PARAMS_CLASS);
                ConsumerUtils.handleByStrategy(body, strategyName, paramsClass);
            }
        };
    }

    @Bean
    public Object countmoneyOrderCreateConsumer(RedisMessageListenerContainer container, MessageListener countmoneyOrderCreateMessageListener) {
        String channel = BroadcastConstants.ORDER_CREATE.EXCHANGE;
        container.addMessageListener(countmoneyOrderCreateMessageListener, new ChannelTopic(channel));
        return new Object();
    }
}
