package com.company.admin;

import java.io.IOException;

import org.beetl.core.resource.WebAppResourceLoader;
import org.beetl.ext.spring.BeetlGroupUtilConfiguration;
import org.beetl.ext.spring.BeetlSpringViewResolver;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternUtils;

import com.company.admin.autoconfigure.BeetlConfiguration;
import com.company.framework.context.SpringContextUtil;

//@EnableEurekaClient
//@EnableFeignClients(basePackages = { "com.company.**.api.feign.**" }) // @FeignClient所在的包
@SpringBootApplication(scanBasePackages = "com.company") // bean扫描路径，需要注意com.company.**.api.feign.fallback也需要扫描，所以配置大点
public class AdminApplication {
	public static void main(String[] args) {
		// SpringApplication.run(WebApplication.class, args);

		SpringApplication springApplication = new SpringApplication(AdminApplication.class);
		// 初始化ApplicationContext，保证在所有bean实例化前面
		springApplication.addInitializers(SpringContextUtil.newInstance());
		springApplication.run(args);
	}

	@Bean(initMethod = "init", name = "beetlConfig")
	public BeetlConfiguration getBeetlGroupUtilConfiguration() {
		BeetlConfiguration beetlGroupUtilConfiguration = new BeetlConfiguration();
		/*
		ResourcePatternResolver patternResolver = ResourcePatternUtils
				.getResourcePatternResolver(new DefaultResourceLoader());
		
		String path = null;
		try {
			// WebAppResourceLoader 配置root路径是关键
			path = patternResolver.getResource("classpath:/").getFile().getPath();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// WebAppResourceLoader 配置root路径是关键
		WebAppResourceLoader webAppResourceLoader = new WebAppResourceLoader(path);
		beetlGroupUtilConfiguration.setResourceLoader(webAppResourceLoader);
		// 读取配置文件信息
*/
		return beetlGroupUtilConfiguration;
	}

	@Bean(name = "beetlViewResolver")
	public BeetlSpringViewResolver getBeetlSpringViewResolver(
			@Qualifier("beetlConfig") BeetlGroupUtilConfiguration beetlGroupUtilConfiguration) {
		BeetlSpringViewResolver beetlSpringViewResolver = new BeetlSpringViewResolver();
		beetlSpringViewResolver.setPrefix("WEB-INF/views/");
		beetlSpringViewResolver.setSuffix(".html");
		beetlSpringViewResolver.setContentType("text/html;charset=UTF-8");
		beetlSpringViewResolver.setOrder(0);
		beetlSpringViewResolver.setConfig(beetlGroupUtilConfiguration);
		return beetlSpringViewResolver;
	}
}