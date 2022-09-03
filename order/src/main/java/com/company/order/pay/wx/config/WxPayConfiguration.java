package com.company.order.pay.wx.config;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(WxPayProperties.class)
public class WxPayConfiguration {
    private final WxPayProperties properties;

    private static Map<String, WxPayProperties.PayConfig> payConfigs;

    @Autowired
    public WxPayConfiguration(WxPayProperties properties) {
        this.properties = properties;
    }
    
    public static WxPayProperties.PayConfig getPayConfig(String appid) {
    	WxPayProperties.PayConfig payConfig = payConfigs.get(appid);
        if (payConfig == null) {
            throw new IllegalArgumentException(String.format("未找到对应appid=[%s]的配置，请核实！", appid));
        }

        return payConfig;
    }
    
    @PostConstruct
    public void init() {
        List<WxPayProperties.PayConfig> configs = this.properties.getConfigs();
        payConfigs = configs.stream().collect(Collectors.toMap(WxPayProperties.PayConfig::getAppId, a->a));
    }
}
