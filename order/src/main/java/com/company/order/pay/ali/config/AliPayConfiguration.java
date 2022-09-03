package com.company.order.pay.ali.config;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(AliPayProperties.class)
public class AliPayConfiguration {
    private final AliPayProperties properties;

    private static Map<String, AliPayProperties.PayConfig> payConfigs;

    @Autowired
    public AliPayConfiguration(AliPayProperties properties) {
        this.properties = properties;
    }
    
    public static AliPayProperties.PayConfig getPayConfig(String appid) {
    	AliPayProperties.PayConfig payConfig = payConfigs.get(appid);
        if (payConfig == null) {
            throw new IllegalArgumentException(String.format("未找到对应appid=[%s]的配置，请核实！", appid));
        }

        return payConfig;
    }
    
    @PostConstruct
    public void init() {
        List<AliPayProperties.PayConfig> configs = this.properties.getConfigs();
        payConfigs = configs.stream().collect(Collectors.toMap(AliPayProperties.PayConfig::getAppId, a->a));
    }
}
