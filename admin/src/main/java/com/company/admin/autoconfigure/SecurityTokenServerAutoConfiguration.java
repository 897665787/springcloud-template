package com.company.admin.autoconfigure;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.company.admin.oss.sts.server.SecurityTokenServer;

@Configuration
public class SecurityTokenServerAutoConfiguration {

	@Bean
	public SecurityTokenServer securityTokenServer() {
		SecurityTokenServer securityTokenServer = new SecurityTokenServer();
		securityTokenServer.setConfigFileName("SecurityTokenServer.json");
		securityTokenServer.setSessionName("xs");
		return securityTokenServer;
	}
}
