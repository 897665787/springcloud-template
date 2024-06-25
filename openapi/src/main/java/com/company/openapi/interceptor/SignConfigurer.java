package com.company.openapi.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@ConditionalOnProperty(prefix = "sign", name = "check", havingValue = "true", matchIfMissing = true)
public class SignConfigurer implements WebMvcConfigurer {

	@Autowired
	private SignInterceptor signInterceptor;
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(signInterceptor).addPathPatterns("/**");
	}
}
