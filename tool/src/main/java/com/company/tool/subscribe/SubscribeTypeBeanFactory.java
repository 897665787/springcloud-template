package com.company.tool.subscribe;

import com.company.framework.context.SpringContextUtil;
import com.company.tool.api.enums.SubscribeEnum;

public final class SubscribeTypeBeanFactory {
	private SubscribeTypeBeanFactory() {
	}

	private static final String SUFFIX = "SubscribeType";

	public static final String NEW_USER_COUPON_PKG_COUPON_REVICE = SubscribeEnum.GROUP.NEW_USER_COUPON_PKG
			+ SubscribeEnum._TYPE.COUPON_REVICE + SUFFIX;
	public static final String NEW_USER_COUPON_PKG_COUPON_USE = SubscribeEnum.GROUP.NEW_USER_COUPON_PKG
			+ SubscribeEnum._TYPE.COUPON_USE + SUFFIX;

	public static SubscribeType from(String group, SubscribeEnum.Type type) {
		String beanName = group + type.getCode() + SUFFIX;
		SubscribeType subscribeType = SpringContextUtil.getBean(beanName, SubscribeType.class);
		return subscribeType;
	}
}
