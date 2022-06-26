package com.company.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication(scanBasePackages = "com.company")
@EnableConfigServer
public class ConfigApplication {
	public static void main(String[] args) {
		 SpringApplication.run(ConfigApplication.class, args);
		
//		SpringApplication springApplication = new SpringApplication(ConfigApplication.class);
		// 初始化ApplicationContext，保证在所有bean实例化前面
//		springApplication.addInitializers(SpringContextUtil.newInstance());
//		springApplication.run(args);
	}
}