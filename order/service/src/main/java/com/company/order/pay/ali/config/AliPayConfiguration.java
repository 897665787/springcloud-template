package com.company.order.pay.ali.config;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(AliPayProperties.class)
public class AliPayConfiguration {

    private AliPayProperties properties;

    public AliPayConfiguration(AliPayProperties properties) {
        this.properties = properties;
    }

    public AliPayProperties.PayConfig getPayConfig(String appid) {
		List<AliPayProperties.PayConfig> payConfigList = properties.getConfigs();
		for (AliPayProperties.PayConfig payConfig : payConfigList) {
			if (payConfig.getAppId().equals(appid)) {
				return payConfig;
			}
		}
		throw new IllegalArgumentException(String.format("未找到对应appid=[%s]的配置，请核实！", appid));
    }
}
