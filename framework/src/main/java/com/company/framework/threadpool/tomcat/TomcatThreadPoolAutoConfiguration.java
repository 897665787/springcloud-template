package com.company.framework.threadpool.tomcat;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TomcatThreadPoolAutoConfiguration {

	@Bean
	public ThreadPoolTomcatWebServerFactoryCustomizer threadPoolTomcatWebServerFactoryCustomizer() {
		return new ThreadPoolTomcatWebServerFactoryCustomizer();
	}
}
