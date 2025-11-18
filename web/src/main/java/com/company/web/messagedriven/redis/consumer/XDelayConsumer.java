package com.company.web.messagedriven.redis.consumer;

import com.company.framework.messagedriven.redis.RedisAutoConfiguration;
import com.company.framework.messagedriven.redis.utils.ConsumerUtils;
import com.company.framework.util.JsonUtil;
import com.company.web.messagedriven.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * Redis延时队列消费者（基于x-delayed实现）
 *
 * @author JQ棣
 */
@Slf4j
@Component
@Conditional(RedisAutoConfiguration.RedisCondition.class)
public class XDelayConsumer {

    private static final String CHANNEL_PREFIX = "mq:channel:";

    @Bean
    public MessageListener xDelayMessageListener() {
        return new MessageListener() {
            @Override
            public void onMessage(Message message, byte[] pattern) {
                String messageBody = new String(message.getBody(), StandardCharsets.UTF_8);
                XDelayConsumer.this.onMessage(messageBody);
            }
        };
    }

    @Bean
    public Object registerXDelayConsumer(RedisMessageListenerContainer container, MessageListener xDelayMessageListener) {
        String channelKey = CHANNEL_PREFIX + Constants.EXCHANGE.XDELAYED;
        container.addMessageListener(xDelayMessageListener, new ChannelTopic(channelKey));
        log.info("注册Redis延时消息监听器，channel: {}", channelKey);
        return new Object();
    }

    public void onMessage(String message) {
        try {
            // 解析消息
            @SuppressWarnings("unchecked")
            Map<String, Object> messageMap = JsonUtil.toEntity(message, Map.class);
            @SuppressWarnings("unchecked")
            Map<String, String> headers = (Map<String, String>) messageMap.get("headers");
            String body = (String) messageMap.get("body");
            String routingKey = (String) messageMap.get("routing_key");

            // 根据路由键过滤消息
            if (routingKey != null && !Constants.QUEUE.XDELAYED.KEY.equals(routingKey)) {
                return;
            }

            ConsumerUtils.handleByStrategy(body, headers);
        } catch (Exception e) {
            log.error("处理Redis延时消息失败", e);
        }
    }
}
