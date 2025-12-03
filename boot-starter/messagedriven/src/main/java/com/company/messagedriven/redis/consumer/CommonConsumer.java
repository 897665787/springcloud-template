package com.company.messagedriven.redis.consumer;

import com.company.messagedriven.constants.HeaderConstants;
import com.company.messagedriven.redis.RedisMQAutoConfiguration;
import com.company.messagedriven.redis.utils.ConsumerUtils;
import com.company.framework.util.JsonUtil;
import com.company.messagedriven.QueueProperties;
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
public class CommonConsumer {

    @Bean
    public MessageListener commonMessageListener() {
        return new MessageListener() {
            @Override
            public void onMessage(Message message, byte[] pattern) {
                String messageBody = new String(message.getBody(), StandardCharsets.UTF_8);
                @SuppressWarnings("unchecked")
                Map<String, Object> messageMap = JsonUtil.toEntity(messageBody, Map.class);

                String body = MapUtils.getString(messageMap, "body");
                String strategyName = MapUtils.getString(messageMap, HeaderConstants.HEADER_STRATEGY_NAME);
                String paramsClass = MapUtils.getString(messageMap, HeaderConstants.HEADER_PARAMS_CLASS);
                ConsumerUtils.handleByStrategy(body, strategyName, paramsClass);
            }
        };
    }

    @Bean
    public Object registerCommonConsumer(RedisMessageListenerContainer container, MessageListener commonMessageListener, QueueProperties queueProperties) {
        String channel = String.format("%s:%s", queueProperties.getExchange().getDirect(), queueProperties.getQueue().getCommon().getKey());
        container.addMessageListener(commonMessageListener, new ChannelTopic(channel));
        return new Object();
    }
}
