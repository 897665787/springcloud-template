package com.company.order.pay.wx.transfer;

import cn.hutool.crypto.SecureUtil;
import com.company.order.pay.wx.OrderResultTransfer;
import com.company.order.pay.wx.config.WxPayConfiguration;
import com.company.order.pay.wx.config.WxPayProperties;
import com.company.order.pay.wx.result.WxPayAppOrderResult;
import com.github.binarywang.wxpay.constant.WxPayConstants.TradeType;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 微信APP支付
 */
@Component(OrderResultTransfer.BEAN_NAME_PREFIX + TradeType.APP)
public class AppOrderResultTransfer implements OrderResultTransfer {

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

		String partnerId = mchId;
		String packageValue = "Sign=WXPay";

		WxPayAppOrderResult result = WxPayAppOrderResult.builder().prepayId(prepayId).partnerId(partnerId).appId(appid)
				.packageValue(packageValue).timeStamp(timestamp).nonceStr(nonceStr).build();
		
		WxPayProperties.MchConfig mchConfig = wxPayConfiguration.getMchConfig(mchId);
		
		StringBuffer signStr = new StringBuffer();
        signStr.append("appid=").append(result.getAppId())
                .append("&noncestr=").append(result.getNonceStr())
                .append("&package=").append(result.getPackageValue())
                .append("&partnerid=").append(result.getPartnerId())
                .append("&prepayid=").append(result.getPrepayId())
                .append("&timestamp=").append(result.getTimeStamp())
                .append("&key=").append(mchConfig.getMchKey());
        result.setSign(SecureUtil.md5(signStr.toString()).toUpperCase());
		return result;
	}
}
