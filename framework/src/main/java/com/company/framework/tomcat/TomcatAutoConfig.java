package com.company.framework.tomcat;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TomcatAutoConfig {
	
	@Bean
	public ThreadPoolTomcatWebServerFactoryCustomizer threadPoolTomcatWebServerFactoryCustomizer() {
		return new ThreadPoolTomcatWebServerFactoryCustomizer();
	}
}