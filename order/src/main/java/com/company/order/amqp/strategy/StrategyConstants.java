package com.company.order.amqp.strategy;

public interface StrategyConstants {
	String USER_STRATEGY = "userStrategy";
	String MAP_STRATEGY = "mapStrategy";
	String XDELAYMESSAGE_STRATEGY = "xDelayMessageStrategy";
	
	String PAY_CLOSE_STRATEGY = "payCloseStrategy";
	String PAY_NOTIFY_STRATEGY = "payNotifyStrategy";
	String REFUND_NOTIFY_STRATEGY = "refundNotifyStrategy";
}
