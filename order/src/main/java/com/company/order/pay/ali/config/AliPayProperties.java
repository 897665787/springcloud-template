package com.company.order.pay.ali.config;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties(prefix = "pay.ali")
public class AliPayProperties {

	private List<PayConfig> configs;

	@Data
	public static class PayConfig {
		/**
		 * 应用appid
		 */
		private String appId;
		/**
		 * 私钥
		 */
		private String privateKey;
		/**
		 * 公钥
		 */
		private String pubKey;
	}

}
