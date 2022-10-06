package com.company.framework.amqp.springevent.delay;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import com.company.common.util.MdcUtil;

import lombok.Data;

@Data
public class DelayedConsumer implements Delayed {
	private Consumer<Long> consumer;
	private volatile long time;
	private final String traceId;
	
	public DelayedConsumer(Consumer<Long> consumer, Integer delaySeconds) {
		this.consumer = consumer;
		this.time = System.currentTimeMillis() + delaySeconds * 1000;
		traceId = MdcUtil.get();
	}
	
	@Override
	public int compareTo(Delayed o) {
		return Long.compare(time, ((DelayedConsumer) o).time);
	}

	@Override
	public long getDelay(TimeUnit unit) {
		return unit.convert(time - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
	}
	
}