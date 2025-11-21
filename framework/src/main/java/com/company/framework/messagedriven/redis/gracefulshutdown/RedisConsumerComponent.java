package com.company.framework.messagedriven.redis.gracefulshutdown;

import com.company.framework.gracefulshutdown.ConsumerComponent;
import com.company.framework.messagedriven.redis.RedisAutoConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Conditional;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

/**
 * Redis 消费者下线
 *
 * @author JQ棣
 */
@Slf4j
@Component
@Conditional(RedisAutoConfiguration.RedisCondition.class)
public class RedisConsumerComponent implements ConsumerComponent {

    @Autowired
    private RedisMessageListenerContainer redisMessageListenerContainer;

    @Override
    public void preStop() {
        // 下线Redis消费者
        redisMessageListenerContainer.stop();
        log.info("Redis消费者已下线");
    }
}
