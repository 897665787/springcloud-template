package com.company.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication(scanBasePackages = "com.company")
public class GatewayApplication {
	public static void main(String[] args) {
		// SpringApplication.run(WebApplication.class, args);
		
		SpringApplication springApplication = new SpringApplication(GatewayApplication.class);
		springApplication.run(args);
	}
}