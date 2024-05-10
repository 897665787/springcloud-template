package com.company.openapi.config;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties(prefix = "sign")
public class SignAccountProperties {
	private Integer reqValidSeconds;
	private List<Account> accounts;

	@Data
	public static class Account {
		/**
		 * appid
		 */
		private String appid;
		/**
		 * 密钥
		 */
		private String appsecret;
	}

}
