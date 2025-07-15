package com.company.order.pay.wx.transfer;

import cn.hutool.crypto.SecureUtil;
import com.company.order.pay.wx.OrderResultTransfer;
import com.company.order.pay.wx.config.WxPayConfiguration;
import com.company.order.pay.wx.config.WxPayProperties;
import com.company.order.pay.wx.result.WxPayMpOrderResult;
import com.github.binarywang.wxpay.constant.WxPayConstants.SignType;
import com.github.binarywang.wxpay.constant.WxPayConstants.TradeType;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 微信小程序支付
 */
@Component(OrderResultTransfer.BEAN_NAME_PREFIX + TradeType.JSAPI)
public class JSAPIOrderResultTransfer implements OrderResultTransfer {

	@Autowired
	private WxPayConfiguration wxPayConfiguration;
	
	@Override
	public Object toPayInfo(String appid, String mchId, String prepayId, String codeUrl,
			String mwebUrl) {
		if (prepayId == null) {
			throw new RuntimeException("未获取到必要的支付参数");
		}
		
		String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
		String nonceStr = RandomStringUtils.randomAlphanumeric(16);

		WxPayMpOrderResult payResult = WxPayMpOrderResult.builder().appId(appid).timeStamp(timestamp).nonceStr(nonceStr)
				.packageValue("prepay_id=" + prepayId).signType(SignType.MD5).build();
		
		WxPayProperties.MchConfig mchConfig = wxPayConfiguration.getMchConfig(mchId);
		
		StringBuffer signStr = new StringBuffer();
        signStr.append("appId=").append(payResult.getAppId())
                .append("&nonceStr=").append(payResult.getNonceStr())
                .append("&package=").append(payResult.getPackageValue())
                .append("&signType=").append(payResult.getSignType())
                .append("&timeStamp=").append(payResult.getTimeStamp())
                .append("&key=").append(mchConfig.getMchKey());
        payResult.setPaySign(SecureUtil.md5(signStr.toString()).toUpperCase());
        
		return payResult;
	}
}
