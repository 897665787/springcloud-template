package com.company.admin.autoconfigure;

import java.io.IOException;

import org.beetl.core.resource.WebAppResourceLoader;
import org.beetl.ext.spring.BeetlGroupUtilConfiguration;
import org.beetl.ext.spring.BeetlSpringViewResolver;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class BeetlAutoConfiguration {

	@Bean(initMethod = "init", name = "beetlConfig")
	public BeetlGroupUtilConfiguration getBeetlGroupUtilConfiguration() {
		BeetlGroupUtilConfiguration beetlGroupUtilConfiguration = new BeetlConfiguration();

		ResourcePatternResolver patternResolver = ResourcePatternUtils
				.getResourcePatternResolver(new DefaultResourceLoader());

		String path = null;
		try {
			// 获取根路径，即resources
			path = patternResolver.getResource("classpath:/").getFile().getPath();
		} catch (IOException e) {
			log.error("获取根路径 error", e);
		}

		// WebAppResourceLoader 配置root路径是关键
		WebAppResourceLoader webAppResourceLoader = new WebAppResourceLoader(path);
		beetlGroupUtilConfiguration.setResourceLoader(webAppResourceLoader);
		// 读取配置文件信息
		return beetlGroupUtilConfiguration;
	}

	@Bean(name = "beetlViewResolver")
	public BeetlSpringViewResolver getBeetlSpringViewResolver(
			@Qualifier("beetlConfig") BeetlGroupUtilConfiguration beetlGroupUtilConfiguration) {
		BeetlSpringViewResolver beetlSpringViewResolver = new BeetlSpringViewResolver();
		beetlSpringViewResolver.setPrefix("templates/");// 设置beetl文件的路径为：resources/templates
		beetlSpringViewResolver.setSuffix(".html");// 设置beetl的后缀设置为html，默认是btl
		beetlSpringViewResolver.setContentType("text/html;charset=UTF-8");
		beetlSpringViewResolver.setOrder(0);
		beetlSpringViewResolver.setConfig(beetlGroupUtilConfiguration);
		return beetlSpringViewResolver;
	}
}
