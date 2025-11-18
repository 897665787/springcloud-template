package com.company.web.messagedriven.redis.consumer;

import com.company.framework.messagedriven.redis.RedisAutoConfiguration;
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
 * Redis秒杀队列消费者
 *
 * @author JQ棣
 */
@Slf4j
@Component
@Conditional(RedisAutoConfiguration.RedisCondition.class)
public class FlashkillConsumer {

    private static final String CHANNEL_PREFIX = "mq:channel:";

    @Bean
    public MessageListener flashkillMessageListener() {
        return new MessageListener() {
            @Override
            public void onMessage(Message message, byte[] pattern) {
                String messageBody = new String(message.getBody(), StandardCharsets.UTF_8);
                FlashkillConsumer.this.onMessage(messageBody);
            }
        };
    }

    @Bean
    public Object registerFlashkillConsumer(RedisMessageListenerContainer container, MessageListener flashkillMessageListener) {
        String channelKey = CHANNEL_PREFIX + Constants.EXCHANGE.DIRECT;
        container.addMessageListener(flashkillMessageListener, new ChannelTopic(channelKey));
        log.info("注册Redis秒杀消息监听器，channel: {}", channelKey);
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
            if (routingKey == null || !Constants.QUEUE.FLASH_KILL.KEY.equals(routingKey)) {
                return;
            }

            // 处理秒杀逻辑
            log.info("收到秒杀消息，headers:{}, body:{}", JsonUtil.toJsonString(headers), body);
            // TODO: 实现具体的秒杀业务逻辑
        } catch (Exception e) {
            log.error("处理Redis秒杀消息失败", e);
        }
    }
}
