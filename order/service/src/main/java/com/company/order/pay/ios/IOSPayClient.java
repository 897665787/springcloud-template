package com.company.order.pay.ios;

import com.company.framework.util.JsonUtil;
import com.company.order.pay.PayFactory;
import com.company.order.pay.core.BasePayClient;
import com.company.order.pay.dto.PayParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Component(PayFactory.IOS_PAYCLIENT)
public class IOSPayClient extends BasePayClient {

	private static final String PAY_CALLBACK_URL = "/server/callback/ios";

	@Value("${template.domain}")
	private String domain;

	@Override
	protected Object requestPayInfo(PayParams payParams) {
        Map<String, String> payParamMap = new HashMap<>();
        payParamMap.put("tradeId", payParams.getOutTradeNo());
		// 以分为单位；多余的小数始终进位，避免造成经济损失
        BigDecimal fee = payParams.getAmount().multiply(new BigDecimal("100")).setScale(0, BigDecimal.ROUND_UP);
        payParamMap.put("fee", fee.toString());
        payParamMap.put("callbackUrl", domain + PAY_CALLBACK_URL);
//        payParamMap.put("passbackParams", xsTrade.getPassbackParams());
        return JsonUtil.toJsonString(payParamMap);
	}

	@Override
	protected void requestRefund(String outTradeNo, String outRefundNo, BigDecimal refundAmount) {
		throw new RuntimeException("iOS不支持退款");
	}

	@Override
	protected void requestPayCloseOrder(String outTradeNo) {
		throw new RuntimeException("iOS不支持关闭订单");
	}
}
