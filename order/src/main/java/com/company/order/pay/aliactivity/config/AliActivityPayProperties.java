package com.company.order.pay.aliactivity.config;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties(prefix = "pay.aliactivity")
public class AliActivityPayProperties {

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
