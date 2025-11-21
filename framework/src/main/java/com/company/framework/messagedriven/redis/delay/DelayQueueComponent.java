package com.company.framework.messagedriven.redis.delay;

import com.company.framework.messagedriven.MessageSender;
import com.company.framework.messagedriven.springevent.delay.DelayedConsumer;
import com.company.framework.trace.TraceManager;
import com.company.framework.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.DelayQueue;
import java.util.function.Consumer;

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
    @Autowired
	private MessageSender messageSender;

	private DelayQueue<DelayedConsumer> delayQueue = new DelayQueue<>();

    public void inqueue(String channel, String messageJson, long executeTime) {
        stringRedisTemplate.opsForZSet().add(DELAY_QUEUE, messageJson, executeTime);
    }

	@Override
	public void run(String... args) throws Exception {
		Thread thread = new Thread(() -> {
			while (true) {
				try {
                    long currentTime = System.currentTimeMillis();
					// 从延迟队列的头部获取已经过期的消息
					// 如果暂时没有过期消息或者队列为空，则take()方法会被阻塞，直到有过期的消息为止
                    // 获取到期的消息（score小于等于当前时间）
                    Set<String> expiredMessages = stringRedisTemplate.opsForZSet().rangeByScore(DELAY_QUEUE, 0, currentTime);
                    if (expiredMessages == null && expiredMessages.isEmpty()) {
                        continue;
                    }
                    for (String message : expiredMessages) {
                        try {
                            // 解析消息
                            @SuppressWarnings("unchecked")
                            Map<String, Object> messageMap = JsonUtil.toEntity(message, Map.class);


                            // 提取channel名称
                            String channel = delayQueueKey.substring(DELAY_QUEUE_PREFIX.length());
//                            String channelKey = channel;

                            // 发布到对应的频道
//                            stringRedisTemplate.convertAndSend(channelKey, message);
                            messageSender.sendNormalMessage(channel);
                            // 从延时队列中移除
                            stringRedisTemplate.opsForZSet().remove(DELAY_QUEUE, message);

                            log.info("延时消息已到期并发送，channel:{}", channel);
                        } catch (Exception e) {
                            log.error("处理延时消息失败：{}", message, e);
                            // 移除异常消息，避免重复处理
                            stringRedisTemplate.opsForZSet().remove(delayQueueKey, message);
                        }
                    }

//					DelayedConsumer delayMessageEvent = delayQueue.take();// 阻塞
//					Consumer<Long> consumer = delayMessageEvent.getConsumer();
//					String traceId = delayMessageEvent.getTraceId();
//					traceManager.put(traceId);
//					long time = delayMessageEvent.getTime();
//					consumer.accept(time);
					traceManager.remove();
				} catch (InterruptedException e) {
					log.error("DelayMessageEvent thread error", e);
				}
			}
		});
		thread.setDaemon(true);
		thread.start();
	}
}
