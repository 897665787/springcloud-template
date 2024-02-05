package com.company.monitor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

import de.codecentric.boot.admin.server.config.EnableAdminServer;

@EnableAdminServer
@EnableEurekaClient
@SpringBootApplication(scanBasePackages = "com.company")
public class MonitorApplication {
	public static void main(String[] args) {
		// SpringApplication.run(MonitorApplication.class, args);
		
		SpringApplication springApplication = new SpringApplication(MonitorApplication.class);
		// 初始化ApplicationContext，保证在所有bean实例化前面
		// springApplication.addInitializers(SpringContextUtil.newInstance());
		springApplication.run(args);
	}
}