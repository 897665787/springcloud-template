package com.company.adminapi.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//@Order(Ordered.LOWEST_PRECEDENCE)
@Order(Ordered.HIGHEST_PRECEDENCE)
@Configuration
@ConditionalOnProperty(prefix = "template.enable", name = "permission", havingValue = "true", matchIfMissing = true)
public class PermissionConfigurer implements WebMvcConfigurer {

	@Autowired
	private PermissionInterceptor permissionInterceptor;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(permissionInterceptor).addPathPatterns("/**");
	}
}
