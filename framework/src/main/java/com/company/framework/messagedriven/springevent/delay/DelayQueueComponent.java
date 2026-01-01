package com.company.framework.messagedriven.springevent.delay;

import java.util.concurrent.DelayQueue;
import java.util.function.Consumer;

import com.company.framework.trace.TraceManager;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@ConditionalOnProperty(prefix = "template.enable", name = "message-driven", havingValue = "springevent")
@RequiredArgsConstructor
public class DelayQueueComponent implements CommandLineRunner {
	private final TraceManager traceManager;

	private DelayQueue<DelayedConsumer> delayQueue = new DelayQueue<>();

	public void inqueue(DelayedConsumer delayedConsumer) {
		delayQueue.offer(delayedConsumer);// 任务入队
	}

	@Override
	public void run(String... args) throws Exception {
		Thread thread = new Thread(() -> {
			while (true) {
				try {
					// 从延迟队列的头部获取已经过期的消息
					// 如果暂时没有过期消息或者队列为空，则take()方法会被阻塞，直到有过期的消息为止
					DelayedConsumer delayMessageEvent = delayQueue.take();// 阻塞
					Consumer<Long> consumer = delayMessageEvent.getConsumer();
					String traceId = delayMessageEvent.getTraceId();
					traceManager.put(traceId);
					long time = delayMessageEvent.getTime();
					consumer.accept(time);
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
