package com.company.zuul.messagedriven;

/**
 * MQ消费策略
 */
public interface BaseStrategy<E> {
	
	void doStrategy(E e);
}
