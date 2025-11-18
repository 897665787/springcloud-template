package com.company.framework.messagedriven.redis.delay;

import com.company.framework.messagedriven.redis.RedisAutoConfiguration;
import com.company.framework.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Conditional;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;

/**
 * Redis延时队列扫描器
 * 定时扫描延时队列，将到期的消息发送到对应的频道
 *
 * @author JQ棣
 */
@Slf4j
@Component
@Conditional(RedisAutoConfiguration.RedisCondition.class)
public class DelayQueueScanner {

    private static final String DELAY_QUEUE_PREFIX = "mq:delay:";
    private static final String CHANNEL_PREFIX = "mq:channel:";

    @Autowired
    private RedisTemplate<String, String> redisMessageTemplate;

    /**
     * 每秒扫描一次延时队列
     */
    @Scheduled(fixedRate = 1000)
    public void scanDelayQueue() {
        try {
            // 获取所有延时队列的key
            Set<String> delayQueueKeys = redisMessageTemplate.keys(DELAY_QUEUE_PREFIX + "*");
            if (delayQueueKeys == null || delayQueueKeys.isEmpty()) {
                return;
            }

            long currentTime = System.currentTimeMillis();
            for (String delayQueueKey : delayQueueKeys) {
                // 获取到期的消息（score小于等于当前时间）
                Set<String> expiredMessages = redisMessageTemplate.opsForZSet()
                        .rangeByScore(delayQueueKey, 0, currentTime);

                if (expiredMessages != null && !expiredMessages.isEmpty()) {
                    for (String message : expiredMessages) {
                        try {
                            // 解析消息
                            @SuppressWarnings("unchecked")
                            Map<String, Object> messageMap = JsonUtil.toEntity(message, Map.class);
                            
                            // 提取channel名称
                            String channel = delayQueueKey.substring(DELAY_QUEUE_PREFIX.length());
                            String channelKey = CHANNEL_PREFIX + channel;

                            // 发布到对应的频道
                            redisMessageTemplate.convertAndSend(channelKey, message);
                            
                            // 从延时队列中移除
                            redisMessageTemplate.opsForZSet().remove(delayQueueKey, message);
                            
                            log.info("延时消息已到期并发送，channel:{}", channel);
                        } catch (Exception e) {
                            log.error("处理延时消息失败：{}", message, e);
                            // 移除异常消息，避免重复处理
                            redisMessageTemplate.opsForZSet().remove(delayQueueKey, message);
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error("扫描延时队列异常", e);
        }
    }
}
