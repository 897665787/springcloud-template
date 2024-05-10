package com.company.openapi.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@ConditionalOnProperty(prefix = "template.sign", name = "check", havingValue = "true", matchIfMissing = true)
public class SignConfigurer implements WebMvcConfigurer {

	@Autowired
	private SignInterceptor signInterceptor;
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(signInterceptor).addPathPatterns("/**");
	}

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		// 跨域配置
		registry.addMapping("/**") // 对那些请求路径有效
				.allowedOrigins("*").allowedHeaders("*").allowedHeaders("*").allowCredentials(true).maxAge(1800L);
	}

}
