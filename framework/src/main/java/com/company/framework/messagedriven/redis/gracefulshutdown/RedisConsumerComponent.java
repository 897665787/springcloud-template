package com.company.framework.messagedriven.redis.gracefulshutdown;

import com.company.framework.gracefulshutdown.ConsumerComponent;
import com.company.framework.messagedriven.redis.RedisAutoConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Conditional;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

/**
 * Redis消息队列消费者优雅下线
 *
 * @author JQ棣
 */
@Slf4j
@Component
@Conditional(RedisAutoConfiguration.RedisCondition.class)
public class RedisConsumerComponent implements ConsumerComponent {

    @Autowired(required = false)
    private RedisMessageListenerContainer redisMessageListenerContainer;

    @Override
    public void preStop() {
        if (redisMessageListenerContainer != null) {
            // 下线Redis消息监听器
            redisMessageListenerContainer.stop();
            log.info("Redis消息队列消费者已下线");
        }
    }
}
