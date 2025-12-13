package com.company.gateway.gracefulshutdown.messagedriven.redis.consumer;

import com.company.gateway.gracefulshutdown.messagedriven.strategy.StrategyConstants;
import com.company.gateway.messagedriven.constants.BroadcastConstants;
import com.company.gateway.messagedriven.constants.HeaderConstants;
import com.company.gateway.messagedriven.redis.RedisMQAutoConfiguration;
import com.company.gateway.messagedriven.redis.utils.ConsumerUtils;
import com.company.gateway.util.JsonUtil;
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
public class RefreshConsumer {

    @Bean
    public MessageListener refreshMessageListener() {
        return new MessageListener() {
            @Override
            public void onMessage(Message message, byte[] pattern) {
                String messageBody = new String(message.getBody(), StandardCharsets.UTF_8);
                @SuppressWarnings("unchecked")
                Map<String, Object> messageMap = JsonUtil.toEntity(messageBody, Map.class);

                String body = MapUtils.getString(messageMap, "body");
                String strategyName = StrategyConstants.REFRESH_STRATEGY;
                String paramsClass = MapUtils.getString(messageMap, HeaderConstants.HEADER_PARAMS_CLASS);
                ConsumerUtils.handleByStrategy(body, strategyName, paramsClass);
            }
        };
    }

    @Bean
    public Object registerRefreshConsumer(RedisMessageListenerContainer container, MessageListener refreshMessageListener) {
        String channel = BroadcastConstants.DEPLOY.EXCHANGE;
        container.addMessageListener(refreshMessageListener, new ChannelTopic(channel));
        return new Object();
    }
}
