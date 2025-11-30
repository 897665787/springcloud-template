package com.company.tool.messagedriven.strategy;

public interface StrategyConstants {
	String CANAL_STRATEGY = "canalStrategy";

	String USER_STRATEGY = "userStrategy";
	String MAP_STRATEGY = "mapStrategy";
	String XDELAYMESSAGE_STRATEGY = "xDelayMessageStrategy";
	String SENDSMS_STRATEGY = "sendSmsStrategy";
	String SENDEMAIL_STRATEGY = "sendEmailStrategy";
	String SENDWEBHOOK_STRATEGY = "sendWebhookStrategy";
	String SENDSUBSCRIBE_STRATEGY = "sendSubscribeStrategy";

	String COUPON_RECEIVE_STRATEGY = "couponReceiveStrategy";
	String COUPON_TOUSE_STRATEGY = "couponTouseStrategy";
	String COUPON_EXPIRE_STRATEGY = "couponExpireStrategy";
}
