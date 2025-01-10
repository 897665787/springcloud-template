package com.company.gateway.messagedriven;

/**
 * MQ消费策略
 */
public interface BaseStrategy<E> {
	
	void doStrategy(E e);
}
