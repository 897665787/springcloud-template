package com.company.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

import com.company.framework.context.SpringContextUtil;

@EnableEurekaClient
@EnableFeignClients(basePackages = { "com.company.**.api.feign.**" }) // @FeignClient所在的包
@SpringBootApplication(scanBasePackages = "com.company") // bean扫描路径，需要注意com.company.**.api.feign.fallback也需要扫描，所以配置大点
public class UserApplication {
	public static void main(String[] args) {
		// SpringApplication.run(WebApplication.class, args);
		
		SpringApplication springApplication = new SpringApplication(UserApplication.class);
		// 初始化ApplicationContext，保证在所有bean实例化前面
		springApplication.addInitializers(SpringContextUtil.newInstance());
		springApplication.run(args);
	}
}
