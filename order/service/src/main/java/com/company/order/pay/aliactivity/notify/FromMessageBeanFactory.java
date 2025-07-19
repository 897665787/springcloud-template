package com.company.order.pay.aliactivity.notify;

import com.company.framework.context.SpringContextUtil;

public class FromMessageBeanFactory {
	private static final String SUFFIX = "FromMessage";

	// 消息通知
	public static final String ORDERMESSAGE_RECEIVED = "alipay.marketing.activity.ordermessage.received" + SUFFIX;
	public static final String ORDERMESSAGE_REFUNDED = "alipay.marketing.activity.ordermessage.refunded" + SUFFIX;

	// 商家券
	public static final String MESSAGE_RECEIVED = "alipay.marketing.activity.message.received" + SUFFIX;

	// 商家券活动
	public static final String MESSAGE_CREATED = "alipay.marketing.activity.message.created" + SUFFIX;
	public static final String MESSAGE_MODIFIED = "alipay.marketing.activity.message.modified" + SUFFIX;
	public static final String MESSAGE_STOPPED = "alipay.marketing.activity.message.stopped" + SUFFIX;
	public static final String MESSAGE_APPENDED = "alipay.marketing.activity.message.appended" + SUFFIX;

	public static FromMessage of(String msgMethod) {
		String beanName = msgMethod + SUFFIX;
		FromMessage fromMessage = SpringContextUtil.getBean(beanName, FromMessage.class);
		return fromMessage;
	}
}
