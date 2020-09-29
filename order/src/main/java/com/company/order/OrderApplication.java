package com.company.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableEurekaClient
@EnableFeignClients(basePackages = { "com.company.**.api.feign.**" }) // @FeignClient所在的包
@SpringBootApplication(scanBasePackages = "com.company") // bean扫描路径，需要注意com.company.**.api.feign.fallback也需要扫描，所以配置大点
public class OrderApplication {
	public static void main(String[] args) {
		SpringApplication.run(OrderApplication.class, args);
	}
}
