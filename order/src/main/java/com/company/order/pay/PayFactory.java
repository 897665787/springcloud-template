package com.company.order.pay;

import com.company.framework.context.SpringContextUtil;
import com.company.order.api.enums.OrderPayEnum;
import com.company.order.pay.core.PayClient;

/**
 * 支付工厂(微信、支付宝、乐刷)
 */
public class PayFactory {
	
	// 支付宝支付（APP）
	public static final String ALI_PAYCLIENT = PayClient.BEAN_NAME_PREFIX + "ali";
	// 支付宝活动支付（APP）
	public static final String ALIACTIVITY_PAYCLIENT = PayClient.BEAN_NAME_PREFIX + "aliactivity";
	// 微信支付（APP、小程序、H5、公众号）
	public static final String WX_PAYCLIENT = PayClient.BEAN_NAME_PREFIX + "wx";
	// IOS支付
	public static final String IOS_PAYCLIENT = PayClient.BEAN_NAME_PREFIX + "ios";
	
	private PayFactory() {
	}
	
	public static PayClient of(OrderPayEnum.Method payMethod) {
		PayClient tradeClient = SpringContextUtil.getBean(PayClient.BEAN_NAME_PREFIX + payMethod.getCode(), PayClient.class);
		return tradeClient;
	}
}
