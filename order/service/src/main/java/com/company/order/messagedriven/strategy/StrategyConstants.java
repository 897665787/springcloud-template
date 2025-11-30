package com.company.order.messagedriven.strategy;

public interface StrategyConstants {
	String CANAL_STRATEGY = "canalStrategy";

	String USER_STRATEGY = "userStrategy";
	String XDELAYMESSAGE_STRATEGY = "xDelayMessageStrategy";

	String PAY_CLOSE_STRATEGY = "payCloseStrategy";
	String PAY_NOTIFY_STRATEGY = "payNotifyStrategy";
	String REFUND_NOTIFY_STRATEGY = "refundNotifyStrategy";

	String ORDERCREATE_SMS_STRATEGY = "orderCreateSmsStrategy";
	String ORDERCREATE_COUNTMONEY_STRATEGY = "orderCreateCountmoneyStrategy";
}
