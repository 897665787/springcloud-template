package com.company.order.pay.ali;

import com.alipay.api.AlipayConstants;

public interface AliConstants {

	String FORMAT = AlipayConstants.FORMAT_JSON;
	String SIGNTYPE = AlipayConstants.SIGN_TYPE_RSA2;
	String CHARSET = AlipayConstants.CHARSET_UTF8;
	
	String TRADE_SUCCESS = "TRADE_SUCCESS";
	
	public interface TradeStatus {
		String SUCCESS = "SUCCESS";
	}
}
