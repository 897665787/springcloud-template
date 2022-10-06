package com.company.framework.amqp.springevent.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.SimpleApplicationEventMulticaster;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * ApplicationListener异步执行配置
 * 
 * @author JQ棣
 *
 */
@Configuration
public class AsyncExecutorConfig {

	@Bean
	public SimpleApplicationEventMulticaster applicationEventMulticaster(
			ThreadPoolTaskExecutor threadPoolTaskExecutor) {
		SimpleApplicationEventMulticaster simpleApplicationEventMulticaster = new SimpleApplicationEventMulticaster();
		simpleApplicationEventMulticaster.setTaskExecutor(threadPoolTaskExecutor);
		return simpleApplicationEventMulticaster;
	}
}