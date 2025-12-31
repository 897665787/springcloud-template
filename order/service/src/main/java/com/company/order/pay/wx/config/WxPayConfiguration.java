package com.company.order.pay.wx.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(WxPayProperties.class)
public class WxPayConfiguration {
	private WxPayProperties properties;

    public WxPayConfiguration(WxPayProperties properties) {
    	this.properties = properties;
    }
    
	public WxPayProperties.PayConfig getPayConfig(String appid) {
		List<WxPayProperties.PayConfig> payConfigList = properties.getConfigs();
		for (WxPayProperties.PayConfig payConfig : payConfigList) {
			if (payConfig.getAppId().equals(appid)) {
				return payConfig;
			}
		}
		throw new IllegalArgumentException(String.format("未找到对应appid=[%s]的配置，请核实！", appid));
	}

	public WxPayProperties.MchConfig getMchConfig(String mchId) {
		List<WxPayProperties.MchConfig> mchConfigList = properties.getMchs();
		for (WxPayProperties.MchConfig mchConfig : mchConfigList) {
			if (mchConfig.getMchId().equals(mchId)) {
				return mchConfig;
			}
		}
		throw new IllegalArgumentException(String.format("未找到对应mchId=[%s]的配置，请核实！", mchId));
	}
}
