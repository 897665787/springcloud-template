package com.company.order.pay.wx.transfer;

import org.springframework.stereotype.Component;

import com.company.common.exception.BusinessException;
import com.company.order.pay.wx.OrderResultTransfer;
import com.company.order.pay.wx.result.WxPayMwebOrderResult;
import com.github.binarywang.wxpay.constant.WxPayConstants.TradeType;

/**
 * 微信H5支付
 */
@Component(OrderResultTransfer.BEAN_NAME_PREFIX + TradeType.MWEB)
public class H5OrderResultTransfer implements OrderResultTransfer {

	@Override
	public Object toPayInfo(String appid, String mchId, String mchKey, String prepayId, String codeUrl,
			String mwebUrl) {
		if (mwebUrl == null) {
			throw new BusinessException(4015, "未获取到必要的支付参数");
		}
		WxPayMwebOrderResult payResult = new WxPayMwebOrderResult(mwebUrl);
		return payResult;
	}
}
