package com.company.framework.messagedriven.redis.delay;

import cn.hutool.core.thread.ThreadUtil;
import com.company.framework.lock.LockClient;
import com.company.framework.messagedriven.MessageSender;
import com.company.framework.messagedriven.constants.HeaderConstants;
import com.company.framework.messagedriven.redis.RedisMQAutoConfiguration;
import com.company.framework.trace.TraceManager;
import com.company.framework.util.JsonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;

@Slf4j
@Component
@Conditional(RedisMQAutoConfiguration.RedisMQCondition.class)
@RequiredArgsConstructor
public class DelayQueueComponent implements CommandLineRunner {
    private static final String DELAY_QUEUE = "mq:delay";

    private final StringRedisTemplate stringRedisTemplate;
    private final TraceManager traceManager;
    private final MessageSender messageSender;
    private final LockClient lockClient;

    public void inqueue(String exchange, String routingKey, String messageJson, long delaySeconds) {
        // 延时消息：存储到 sorted set，使用时间戳作为score
        long executeTime = System.currentTimeMillis() + delaySeconds * 1000L;
        DelayedConsumer delayedConsumer = new DelayedConsumer(exchange, routingKey, messageJson);
        String delayedJsonStr = JsonUtil.toJsonString(delayedConsumer);
        stringRedisTemplate.opsForZSet().add(DELAY_QUEUE, delayedJsonStr, executeTime);
    }

    @Override
    public void run(String... args) throws Exception {
        Thread thread = new Thread(() -> {
            while (true) {
                try {
                    lockClient.doInLock("lock:mq:delay", () -> {
                        long currentTime = System.currentTimeMillis();
                        // 获取到期的消息（score小于等于当前时间）
                        Set<String> delayedJsonStrSet = stringRedisTemplate.opsForZSet().rangeByScore(DELAY_QUEUE, 0, currentTime);
                        if (delayedJsonStrSet == null || delayedJsonStrSet.isEmpty()) {
                            ThreadUtil.sleep(1000);
                            return null;
                        }
                        for (String delayedJsonStr : delayedJsonStrSet) {
                            DelayedConsumer delayedConsumer = JsonUtil.toEntity(delayedJsonStr, DelayedConsumer.class);

                            String messageJson = delayedConsumer.getMessageJson();
                            Map<String, Object> messageMap = JsonUtil.toEntity(messageJson, Map.class);

                            String traceId = MapUtils.getString(messageMap, HeaderConstants.HEADER_MESSAGE_ID);
                            traceManager.put(traceId);

                            String exchange = delayedConsumer.getExchange();
                            String routingKey = delayedConsumer.getRoutingKey();
                            String strategyName = MapUtils.getString(messageMap, HeaderConstants.HEADER_STRATEGY_NAME);

                            String body = MapUtils.getString(messageMap, "body");
                            Map<String, Object> toJson = JsonUtil.toEntity(body, Map.class);
                            messageSender.sendNormalMessage(strategyName, toJson, exchange, routingKey);
                            traceManager.remove();
                        }
                        // 从延时队列中移除
                        stringRedisTemplate.opsForZSet().remove(DELAY_QUEUE, delayedJsonStrSet.toArray());
                        return null;
                    });
                } catch (Exception e) {
                    log.error("rangeByScore thread error", e);
                }
            }
        });
        thread.setDaemon(true);
        thread.start();
    }
}
