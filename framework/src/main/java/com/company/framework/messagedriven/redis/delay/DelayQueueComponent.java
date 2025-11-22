package com.company.framework.messagedriven.redis.delay;

import com.company.framework.lock.LockClient;
import com.company.framework.messagedriven.MessageSender;
import com.company.framework.messagedriven.constants.HeaderConstants;
import com.company.framework.trace.TraceManager;
import com.company.framework.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.DelayQueue;

@Slf4j
@Component
@ConditionalOnProperty(prefix = "template.enable", name = "message-driven", havingValue = "redis")
public class DelayQueueComponent implements CommandLineRunner {
    private static final String DELAY_QUEUE = "mq:delay";

    @Autowired
    @Qualifier("mqStringRedisTemplate")
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
	private TraceManager traceManager;
    @Lazy
    @Autowired
	private MessageSender messageSender;
    @Autowired
    private LockClient lockClient;

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
                    long currentTime = System.currentTimeMillis();
                    // 获取到期的消息（score小于等于当前时间）
                    Set<String> delayedJsonStrSet = stringRedisTemplate.opsForZSet().rangeByScore(DELAY_QUEUE, 0, currentTime);
                    if (delayedJsonStrSet == null || delayedJsonStrSet.isEmpty()) {
                        Thread.sleep(1000);
                        continue;
                    }
                    traceManager.put();
                    for (String delayedJsonStr : delayedJsonStrSet) {
                        DelayedConsumer delayedConsumer = JsonUtil.toEntity(delayedJsonStr, DelayedConsumer.class);

                        String messageJson = delayedConsumer.getMessageJson();
                        // 解析消息
                        @SuppressWarnings("unchecked")
                        Map<String, Object> messageMap = JsonUtil.toEntity(messageJson, Map.class);

                        String exchange = delayedConsumer.getExchange();
                        String routingKey = delayedConsumer.getRoutingKey();
                        String strategyName = MapUtils.getString(messageMap, HeaderConstants.HEADER_STRATEGY_NAME);
                        String body = MapUtils.getString(messageMap, "body");
                        Map<String, Object> toJson = JsonUtil.toEntity(body, Map.class);
                        messageSender.sendNormalMessage(strategyName, toJson, exchange, routingKey);
                    }
                    // 从延时队列中移除
                    stringRedisTemplate.opsForZSet().remove(DELAY_QUEUE, delayedJsonStrSet);
					traceManager.remove();
				} catch (Exception e) {
					log.error("DelayQueueComponent thread error", e);
				}
			}
		});
		thread.setDaemon(true);
		thread.start();
	}

    public static void main(String[] args) {
        String channel = "mq:delay";
        String[] split = StringUtils.split(channel, ":");
        System.out.println(split[0]);
        System.out.println(split[1]);
        System.out.println(split.length);
    }
}
