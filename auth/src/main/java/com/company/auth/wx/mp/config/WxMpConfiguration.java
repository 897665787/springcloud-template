package com.company.auth.wx.mp.config;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import com.google.common.collect.Maps;

import me.chanjar.weixin.common.error.WxRuntimeException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.config.WxMpConfigStorage;
import me.chanjar.weixin.mp.config.impl.WxMpDefaultConfigImpl;

/**
 * @author <a href="https://github.com/binarywang">Binary Wang</a>
 */
@Configuration
@EnableConfigurationProperties(WxMpProperties.class)
public class WxMpConfiguration {
    private final WxMpProperties properties;

    private static Map<String, WxMpService> mpServices;

    @Autowired
    public WxMpConfiguration(WxMpProperties properties) {
        this.properties = properties;
    }

	public static WxMpService getMpService0() {
		Entry<String, WxMpService> wxMpServiceEntry = mpServices.entrySet().stream().findFirst().orElse(null);
		if (wxMpServiceEntry == null) {
			throw new IllegalArgumentException(String.format("未找到对应的配置，请核实！"));
		}
        
        return wxMpServiceEntry.getValue();
    }
    
    public static WxMpService getMpService(String appid) {
        WxMpService wxService = mpServices.get(appid);
        if (wxService == null) {
            throw new IllegalArgumentException(String.format("未找到对应appid=[%s]的配置，请核实！", appid));
        }

        return wxService;
    }

    @PostConstruct
    public void init() {
        List<WxMpProperties.MpConfig> configs = this.properties.getConfigs();
        if (configs == null) {
            throw new WxRuntimeException("大哥，拜托先看下项目首页的说明（readme文件），添加下相关配置，注意别配错了！");
        }

		mpServices = configs.stream().collect(Collectors.toMap(a -> a.getAppId(), a -> {
			WxMpDefaultConfigImpl config = new WxMpDefaultConfigImpl();
//          WxMpDefaultConfigImpl config = new WxMpRedisConfigImpl(new JedisPool());
			// 使用上面的配置时，需要同时引入jedis-lock的依赖，否则会报类无法找到的异常
			config.setAppId(a.getAppId());
			config.setSecret(a.getSecret());
			config.setToken(a.getToken());
			config.setAesKey(a.getAesKey());

			WxMpService service = new WxMpServiceImpl();

			Map<String, WxMpConfigStorage> configStorage = Maps.newHashMap();
			configStorage.put(a.getAppId(), config);
			service.setMultiConfigStorages(configStorage);
			return service;
		}, (o, n) -> o));
    }
}
