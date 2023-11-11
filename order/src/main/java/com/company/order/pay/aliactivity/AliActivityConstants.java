package com.company.order.pay.aliactivity;

import com.alipay.api.AlipayConstants;

public interface AliActivityConstants {

	String FORMAT = AlipayConstants.FORMAT_JSON;
	String SIGNTYPE = AlipayConstants.SIGN_TYPE_RSA2;
	String CHARSET = AlipayConstants.CHARSET_UTF8;
	
	String TRADE_SUCCESS = "TRADE_SUCCESS";
	String TRADE_CLOSED = "TRADE_CLOSED";
	
	public interface TradeStatus {
		String SUCCESS = "SUCCESS";
	}
	
	public interface Response {
		String CODE_SUCCESS = "10000";
		String CODE_40004 = "40004";
		
		String MSG_SUCCESS = "Success";
		String MSG_BUSINESS_FAILED = "Business Failed";
	}
	
}
