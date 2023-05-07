package com.company.order.pay.wx.config;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties(prefix = "pay.wx")
public class WxPayProperties {

	private List<MchConfig> mchs;
	private List<PayConfig> configs;

	@Data
	public static class MchConfig {
		/**
		 * 微信支付商户号
		 */
		private String mchId;
		
		/**
		 * 微信支付商户密钥
		 */
		private String mchKey;

		/**
		 * apiclient_cert.p12文件的绝对路径，或者如果放在项目中，请以classpath:开头指定
		 */
		private String keyPath;
	}
	
	@Data
	public static class PayConfig {
		/**
		 * 设置微信公众号或者小程序等的appid
		 */
		private String appId;

		/**
		 * 微信支付商户号
		 */
		private String mchId;
		
		private String tradeType;
	}
}