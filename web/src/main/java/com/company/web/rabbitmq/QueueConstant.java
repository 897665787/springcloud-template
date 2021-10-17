package com.company.web.rabbitmq;

public interface QueueConstant {
	
	String EXCHANGE = "exchange.direct";
	
	interface COMMON_QUEUE {
		String ROUTING_KEY = "routingkey.common";
		String QUEUE = "queue.common";
	}
	
	interface DELAY_QUEUE {
		String ROUTING_KEY = "routingkey.delay";
		String DEAD_QUEUE = "dead.queue.delay";
	}
}
