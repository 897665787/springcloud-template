package com.company.order.pay.ios;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.company.order.api.response.PayTradeStateResp;
import com.company.order.pay.PayFactory;
import com.company.order.pay.core.BasePayClient;
import com.company.order.pay.dto.PayParams;

@Component(PayFactory.IOS_PAYCLIENT)
public class IOSPayClient extends BasePayClient {

	private static final String PAY_CALLBACK_URL = "/server/callback/ios";

	@Value("${template.domain}")
	private String domain;

	@Override
	public Object getPayInfo(String outTradeNo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PayTradeStateResp queryTradeState(String outTradeNo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Object requestPayInfo(PayParams payParams) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void requestRefund(String outTradeNo, String outRefundNo, BigDecimal refundAmount) {
		// TODO Auto-generated method stub
	}

	@Override
	protected void requestPayCloseOrder(String outTradeNo) {
		// TODO Auto-generated method stub

	}
}
