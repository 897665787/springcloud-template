package com.company.order.pay.aliactivity.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(AliActivityPayProperties.class)
public class AliActivityPayConfiguration {
	
    private final AliActivityPayProperties properties;

    public AliActivityPayConfiguration(AliActivityPayProperties properties) {
        this.properties = properties;
    }
    
    public AliActivityPayProperties.PayConfig getPayConfig(String appid) {
		List<AliActivityPayProperties.PayConfig> payConfigList = properties.getConfigs();
		for (AliActivityPayProperties.PayConfig payConfig : payConfigList) {
			if (payConfig.getAppId().equals(appid)) {
				return payConfig;
			}
		}
		throw new IllegalArgumentException(String.format("未找到对应appid=[%s]的配置，请核实！", appid));
    }
}
