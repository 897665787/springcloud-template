package com.company.order.pay.wx.mock;

import java.nio.charset.Charset;
import java.util.Date;
import java.util.Map;
import java.util.Random;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.constant.WxPayConstants.TradeType;
import com.github.binarywang.wxpay.util.SignUtils;

import cn.hutool.core.io.FileUtil;
import cn.hutool.http.HttpUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NotifyMock {

	public static void payNotify(WxPayConfig config, WxPayUnifiedOrderRequest request) {
		String xmlData = FileUtil.readString("classpath:pay/mock-notify-template.xml", Charset.forName("UTF-8"));

		xmlData = xmlData.replace("{appid}", config.getAppId());
		xmlData = xmlData.replace("{mch_id}", config.getMchId());

		xmlData = xmlData.replace("{nonce_str}", request.getNonceStr());
		xmlData = xmlData.replace("{openid}", request.getOpenid());
		xmlData = xmlData.replace("{out_trade_no}", request.getOutTradeNo());
		xmlData = xmlData.replace("{total_fee}", request.getTotalFee().toString());
		xmlData = xmlData.replace("{time_end}", DateFormatUtils.format(new Date(), "yyyyMMddHHmmss"));
		xmlData = xmlData.replace("{trade_type}", request.getTradeType());
		String tranId = "420000080820201224441" + new Random().nextInt(83262531);
		xmlData = xmlData.replace("{transaction_id}", tranId);


		// 解析微信支付返回报文
		WxPayOrderNotifyResult orderNotifyResult = WxPayOrderNotifyResult.fromXML(xmlData);
		// 校验返回结果签名
		Map<String, String> map = orderNotifyResult.toMap();
		String sign = SignUtils.createSign(map, null, config.getMchKey(), new String[0]);

		xmlData = xmlData.replace("{sign}", sign);
		log.info("xmlData:{}", xmlData);

//		WxPayOrderNotifyResult orderNotifyResult2 = WxPayOrderNotifyResult.fromXML(xmlData);
//		Map<String, String> map2 = orderNotifyResult2.toMap();
//		boolean checkSign = SignUtils.checkSign(map2, null, config.getMchKey());
//		System.out.println("checkSign:" + checkSign);

		String result = HttpUtil.post(request.getNotifyUrl(), xmlData);
		log.info("mock payNotify:{}", result);
	}

	public static void main(String[] args) {
		WxPayConfig config = new WxPayConfig();
		config.setAppId("wx8888888888888888");
		config.setMchId("1900000109");
		config.setMchKey("543543545555555555435345");

		WxPayUnifiedOrderRequest request = new WxPayUnifiedOrderRequest();
		request.setTotalFee(2000);
		request.setOutTradeNo("156115651616161");
		request.setNotifyUrl("http://localhost:7020/openapi/notify/wxPay");
		request.setTradeType(TradeType.JSAPI);
		request.setOpenid("oQvXm5d0q12233PunR1Y-oEr3ZmQ");
		request.setNonceStr(RandomStringUtils.randomAlphanumeric(16));

		NotifyMock.payNotify(config, request);
	}
}
