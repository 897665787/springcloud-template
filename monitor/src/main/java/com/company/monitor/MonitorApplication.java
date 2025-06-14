package com.company.monitor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

import de.codecentric.boot.admin.server.config.EnableAdminServer;

@EnableAdminServer
@EnableEurekaClient
@SpringBootApplication
public class MonitorApplication {
	public static void main(String[] args) {
		SpringApplication.run(MonitorApplication.class, args);
	}
}