package com.company.zuul;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;


@EnableEurekaClient
@SpringBootApplication(scanBasePackages = "com.company")
@EnableZuulProxy
public class ZuulApplication {
	public static void main(String[] args) {
		// SpringApplication.run(WebApplication.class, args);
		
		SpringApplication springApplication = new SpringApplication(ZuulApplication.class);
		springApplication.run(args);
	}
}