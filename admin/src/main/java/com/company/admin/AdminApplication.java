package com.company.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.company.common.constant.CommonConstants;
import com.company.framework.context.SpringContextUtil;

//@EnableEurekaClient
//@EnableFeignClients(basePackages = { CommonConstants.BASE_PACKAGES + ".**.api.feign.**" }) // @FeignClient所在的包
@SpringBootApplication(scanBasePackages = CommonConstants.BASE_PACKAGE) // bean扫描路径，需要注意com.company.**.api.feign.fallback也需要扫描，所以配置大点
public class AdminApplication {
	public static void main(String[] args) {
		// SpringApplication.run(AdminApplication.class, args);

		SpringApplication springApplication = new SpringApplication(AdminApplication.class);
		// 初始化ApplicationContext，保证在所有bean实例化前面
		springApplication.addInitializers(SpringContextUtil.newInstance());
		springApplication.run(args);
	}
}