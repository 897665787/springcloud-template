package com.company.zuul.amqp;

/**
 * MQ消费策略
 */
public interface BaseStrategy<E> {
	
	void doStrategy(E e);
}
