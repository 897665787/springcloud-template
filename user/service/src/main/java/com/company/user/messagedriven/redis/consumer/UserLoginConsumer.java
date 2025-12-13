package com.company.user.messagedriven.redis.consumer;

import com.company.framework.messagedriven.constants.BroadcastConstants;
import com.company.framework.messagedriven.constants.HeaderConstants;
import com.company.framework.messagedriven.redis.RedisMQAutoConfiguration;
import com.company.framework.messagedriven.redis.utils.ConsumerUtils;
import com.company.framework.util.JsonUtil;
import com.company.user.messagedriven.strategy.StrategyConstants;
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
public class UserLoginConsumer {

    @Bean
    public MessageListener loginRecordMessageListener() {
        return new MessageListener() {
            @Override
            public void onMessage(Message message, byte[] pattern) {
                String messageBody = new String(message.getBody(), StandardCharsets.UTF_8);
                @SuppressWarnings("unchecked")
                Map<String, Object> messageMap = JsonUtil.toEntity(messageBody, Map.class);

                String body = MapUtils.getString(messageMap, "body");
                String strategyName = StrategyConstants.LOGINRECORD_STRATEGY;
                String paramsClass = MapUtils.getString(messageMap, HeaderConstants.HEADER_PARAMS_CLASS);
                ConsumerUtils.handleByStrategy(body, strategyName, paramsClass);
            }
        };
    }

    @Bean
    public Object registerLoginRecordConsumer(RedisMessageListenerContainer container, MessageListener loginRecordMessageListener) {
        String channel = BroadcastConstants.USER_LOGIN.EXCHANGE;
        container.addMessageListener(loginRecordMessageListener, new ChannelTopic(channel));
        return new Object();
    }

    @Bean
    public MessageListener userDeviceMessageListener() {
        return new MessageListener() {
            @Override
            public void onMessage(Message message, byte[] pattern) {
                String messageBody = new String(message.getBody(), StandardCharsets.UTF_8);
                @SuppressWarnings("unchecked")
                Map<String, Object> messageMap = JsonUtil.toEntity(messageBody, Map.class);

                String body = MapUtils.getString(messageMap, "body");
                String strategyName = StrategyConstants.USERDEVICE_LOGIN_STRATEGY;
                String paramsClass = MapUtils.getString(messageMap, HeaderConstants.HEADER_PARAMS_CLASS);
                ConsumerUtils.handleByStrategy(body, strategyName, paramsClass);
            }
        };
    }

    @Bean
    public Object registerUserDeviceConsumer(RedisMessageListenerContainer container, MessageListener userDeviceMessageListener) {
        String channel = BroadcastConstants.USER_LOGIN.EXCHANGE;
        container.addMessageListener(userDeviceMessageListener, new ChannelTopic(channel));
        return new Object();
    }
}
