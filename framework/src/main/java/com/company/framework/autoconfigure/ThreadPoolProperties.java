package com.company.framework.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties(prefix = "template.threadpool")
public class ThreadPoolProperties {
	private Integer corePoolSize;
	private Integer maxPoolSize;
	private Integer queueCapacity;
	private Integer keepAliveSeconds;
	private String threadNamePrefix;
	private Boolean waitForTasksToCompleteOnShutdown;
	private Integer awaitTerminationSeconds;
}
