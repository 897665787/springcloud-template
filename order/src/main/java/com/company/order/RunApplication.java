package com.company.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
//@EnableElasticsearchRepositories
@SpringBootApplication(scanBasePackages = "com.company") // bean扫描路径，需要注意com.company.**.api.feign.fallback也需要扫描，所以配置大点
public class RunApplication {
	public static void main(String[] args) {
		SpringApplication.run(RunApplication.class, args);
	}
}
