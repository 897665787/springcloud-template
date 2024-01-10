package com.company.order;

import org.frameworkset.elasticsearch.boot.BBossESAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

import com.company.framework.context.SpringContextUtil;

@EnableEurekaClient
@EnableFeignClients(basePackages = { "com.company.**.api.feign.**" }) // @FeignClient所在的包
@SpringBootApplication(scanBasePackages = "com.company", exclude = { BBossESAutoConfiguration.class, RabbitAutoConfiguration.class }) // bean扫描路径，需要注意com.company.**.api.feign.fallback也需要扫描，所以配置大点
public class OrderApplication {
	public static void main(String[] args) {
		// SpringApplication.run(OrderApplication.class, args);
		
		SpringApplication springApplication = new SpringApplication(OrderApplication.class);
		// 初始化ApplicationContext，保证在所有bean实例化前面
		springApplication.addInitializers(SpringContextUtil.newInstance());
		springApplication.run(args);
	}
}
