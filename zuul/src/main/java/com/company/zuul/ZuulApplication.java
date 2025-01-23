package com.company.zuul;

import org.apache.rocketmq.spring.autoconfigure.RocketMQAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

import com.company.common.constant.CommonConstants;
import com.company.zuul.context.SpringContextUtil;


@EnableEurekaClient
@SpringBootApplication(scanBasePackages = CommonConstants.BASE_PACKAGE, exclude = { RabbitAutoConfiguration.class, RocketMQAutoConfiguration.class })
@EnableZuulProxy
public class ZuulApplication {
	public static void main(String[] args) {
		// SpringApplication.run(ZuulApplication.class, args);
		
		SpringApplication springApplication = new SpringApplication(ZuulApplication.class);
		// 初始化ApplicationContext，保证在所有bean实例化前面
		springApplication.addInitializers(SpringContextUtil.newInstance());
		springApplication.run(args);
	}
}