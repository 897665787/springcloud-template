package com.company.order.pay.wx.transfer;

import com.company.order.pay.wx.OrderResultTransfer;
import com.company.order.pay.wx.result.WxPayMwebOrderResult;
import com.github.binarywang.wxpay.constant.WxPayConstants.TradeType;
import org.springframework.stereotype.Component;

/**
 * 微信H5支付
 */
@Component(OrderResultTransfer.BEAN_NAME_PREFIX + TradeType.MWEB)
public class H5OrderResultTransfer implements OrderResultTransfer {

	@Override
	public Object toPayInfo(String appid, String mchId, String prepayId, String codeUrl,
			String mwebUrl) {
		if (mwebUrl == null) {
			throw new RuntimeException("未获取到必要的支付参数");
		}
		WxPayMwebOrderResult payResult = new WxPayMwebOrderResult(mwebUrl);
		return payResult;
	}
}
