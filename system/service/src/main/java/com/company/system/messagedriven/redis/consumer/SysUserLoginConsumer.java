package com.company.system.messagedriven.redis.consumer;

import com.company.framework.messagedriven.constants.FanoutConstants;
import com.company.framework.messagedriven.constants.HeaderConstants;
import com.company.framework.messagedriven.redis.RedisAutoConfiguration;
import com.company.framework.messagedriven.redis.utils.ConsumerUtils;
import com.company.framework.util.JsonUtil;
import com.company.system.messagedriven.strategy.StrategyConstants;
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
@Conditional(RedisAutoConfiguration.RedisCondition.class)
public class SysUserLoginConsumer {

    @Bean
    public MessageListener loginRecordMessageListener() {
        return new MessageListener() {
            @Override
            public void onMessage(Message message, byte[] pattern) {
                String messageBody = new String(message.getBody(), StandardCharsets.UTF_8);
                @SuppressWarnings("unchecked")
                Map<String, Object> messageMap = JsonUtil.toEntity(messageBody, Map.class);

                String body = MapUtils.getString(messageMap, "body");
                String strategyName = StrategyConstants.SYS_LOGINRECORD_STRATEGY;
                String paramsClass = MapUtils.getString(messageMap, HeaderConstants.HEADER_PARAMS_CLASS);
                ConsumerUtils.handleByStrategy(body, strategyName, paramsClass);
            }
        };
    }

    @Bean
    public Object registerLoginRecordConsumer(RedisMessageListenerContainer container, MessageListener loginRecordMessageListener) {
        String channel = FanoutConstants.SYS_USER_LOGIN.EXCHANGE;
        container.addMessageListener(loginRecordMessageListener, new ChannelTopic(channel));
        return new Object();
    }

    @Bean
    public MessageListener incrExpireLoginTimesMessageListener() {
        return new MessageListener() {
            @Override
            public void onMessage(Message message, byte[] pattern) {
                String messageBody = new String(message.getBody(), StandardCharsets.UTF_8);
                @SuppressWarnings("unchecked")
                Map<String, Object> messageMap = JsonUtil.toEntity(messageBody, Map.class);

                String body = MapUtils.getString(messageMap, "body");
                String strategyName = StrategyConstants.INCR_EXPIRELOGINTIMES_STRATEGY;
                String paramsClass = MapUtils.getString(messageMap, HeaderConstants.HEADER_PARAMS_CLASS);
                ConsumerUtils.handleByStrategy(body, strategyName, paramsClass);
            }
        };
    }

    @Bean
    public Object registerIncrExpireLoginTimesConsumer(RedisMessageListenerContainer container, MessageListener incrExpireLoginTimesMessageListener) {
        String channel = FanoutConstants.SYS_USER_LOGIN.EXCHANGE;
        container.addMessageListener(incrExpireLoginTimesMessageListener, new ChannelTopic(channel));
        return new Object();
    }
}