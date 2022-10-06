package com.company.framework.interceptor;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@ConditionalOnProperty(prefix = "template.enable", name = "access-control", havingValue = "true", matchIfMissing = true)
public class AccessControlConfigurer implements WebMvcConfigurer {

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new AccessControlInterceptor()).addPathPatterns("/**");
	}

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		// 跨域配置
		registry.addMapping("/**") // 对那些请求路径有效
				.allowedOrigins("*").allowedHeaders("*").allowedHeaders("*").allowCredentials(true).maxAge(1800L);
	}

}
