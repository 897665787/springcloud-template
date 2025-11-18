package com.company.web.messagedriven.redis.consumer;

import com.company.framework.messagedriven.redis.RedisAutoConfiguration;
import com.company.framework.messagedriven.redis.utils.ConsumerUtils;
import com.company.framework.util.JsonUtil;
import com.company.web.messagedriven.Constants;
import com.company.web.messagedriven.strategy.dto.UserMQDto;
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
 * Redis订单创建消费者
 *
 * @author JQ棣
 */
@Slf4j
@Component
@Conditional(RedisAutoConfiguration.RedisCondition.class)
public class OrderCreateConsumer {

    private static final String CHANNEL_PREFIX = "mq:channel:";

    @Bean
    public MessageListener orderCreateMessageListener() {
        return new MessageListener() {
            @Override
            public void onMessage(Message message, byte[] pattern) {
                String messageBody = new String(message.getBody(), StandardCharsets.UTF_8);
                OrderCreateConsumer.this.onMessage(messageBody);
            }
        };
    }

    @Bean
    public Object registerOrderCreateConsumer(RedisMessageListenerContainer container, MessageListener orderCreateMessageListener) {
        String channelKey = CHANNEL_PREFIX + Constants.EXCHANGE.DIRECT;
        container.addMessageListener(orderCreateMessageListener, new ChannelTopic(channelKey));
        log.info("注册Redis订单创建消息监听器，channel: {}", channelKey);
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

            // 使用自定义Consumer处理
            ConsumerUtils.handleByConsumer(body, headers, (UserMQDto userMQDto) -> {
                log.info("订单创建通知--短信:{}", JsonUtil.toJsonString(userMQDto));
                // TODO: 实现发送短信逻辑
            });

            ConsumerUtils.handleByConsumer(body, headers, (UserMQDto userMQDto) -> {
                log.info("订单创建通知--扣钱:{}", JsonUtil.toJsonString(userMQDto));
                // TODO: 实现扣款逻辑
            });
        } catch (Exception e) {
            log.error("处理Redis订单创建消息失败", e);
        }
    }
}
