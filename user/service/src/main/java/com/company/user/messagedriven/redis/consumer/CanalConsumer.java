package com.company.user.messagedriven.redis.consumer;

import com.company.framework.messagedriven.constants.FanoutConstants;
import com.company.framework.messagedriven.redis.RedisAutoConfiguration;
import com.company.framework.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;

@Slf4j
@Component
@Conditional(RedisAutoConfiguration.RedisCondition.class)
public class CanalConsumer {

    @Bean
    public MessageListener userMessageListener() {
        return new MessageListener() {
            @Override
            public void onMessage(Message message, byte[] pattern) {
                String messageBody = new String(message.getBody(), StandardCharsets.UTF_8);
                handleByConsumer(messageBody, new Consumer<String>() {
                    @Override
                    public void accept(String params) {
                        log.info("user params:{}", JsonUtil.toJsonString(params));
                    }
                });
            }
        };
    }

    @Bean
    public Object registerUserConsumer(RedisMessageListenerContainer container, MessageListener userMessageListener) {
        String channel = FanoutConstants.CANAL.EXCHANGE;
        container.addMessageListener(userMessageListener, new ChannelTopic(channel));
        return new Object();
    }

    private static void handleByConsumer(String jsonStrMsg, Consumer<String> consumer) {
        if (jsonStrMsg == null) {
            log.info("jsonStrMsg is null");
            return;
        }

        long start = System.currentTimeMillis();
        try {
            log.info("jsonStrMsg:{}", jsonStrMsg);
            consumer.accept(jsonStrMsg);
        } catch (RuntimeException e) {
            // 业务异常一般是校验不通过，可以当做成功处理
            log.warn("RuntimeException", e);
        } catch (Exception e) {
            log.error("accept error", e);
        } finally {
            log.info("耗时:{}ms", System.currentTimeMillis() - start);
        }
    }
}