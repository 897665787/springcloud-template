package com.company.framework.autoconfigure;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.company.framework.threadpool.tomcat.ThreadPoolTomcatWebServerFactoryCustomizer;

@Configuration
public class TomcatThreadPoolAutoConfiguration {
	
	@Bean
	public ThreadPoolTomcatWebServerFactoryCustomizer threadPoolTomcatWebServerFactoryCustomizer() {
		return new ThreadPoolTomcatWebServerFactoryCustomizer();
	}
}