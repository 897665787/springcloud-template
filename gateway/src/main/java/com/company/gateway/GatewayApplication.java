package com.company.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

import com.company.common.constant.CommonConstants;
import com.company.gateway.context.SpringContextUtil;

@EnableEurekaClient
@SpringBootApplication(scanBasePackages = CommonConstants.BASE_PACKAGE, exclude = { RabbitAutoConfiguration.class })
public class GatewayApplication {
	public static void main(String[] args) {
		// SpringApplication.run(GatewayApplication.class, args);
		
		SpringApplication springApplication = new SpringApplication(GatewayApplication.class);
		// 初始化ApplicationContext，保证在所有bean实例化前面
		springApplication.addInitializers(SpringContextUtil.newInstance());
		springApplication.run(args);
	}
}