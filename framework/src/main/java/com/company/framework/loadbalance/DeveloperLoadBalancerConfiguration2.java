package com.company.framework.loadbalance;

import com.netflix.loadbalancer.IRule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

//@Order(-2147482649)
//@Configuration
//@ConditionalOnDiscoveryEnabled
public class DeveloperLoadBalancerConfiguration2 {
//    @Bean
//    @Lazy
//    public IRule myRule() {
//        DeveloperRule rule = new DeveloperRule();
//        return rule;
//    }

//    @Bean
////    @ConditionalOnMissingBean
//    public IRule ribbonRule(IClientConfig config) {
//        ZoneAvoidanceRule rule = new ZoneAvoidanceRule();
//        rule.initWithNiwsConfig(config);
//        return rule;
//    }

//	@Bean

    /// /	@ConditionalOnMissingBean
//	public PropertiesFactory propertiesFactory() {
//		PropertiesFactory propertiesFactory = new PropertiesFactory();
//		return propertiesFactory;
//	}

//    @Bean
//    @ConditionalOnMissingBean
//    public IClientConfig ribbonClientConfig() {
//        DefaultClientConfigImpl config = new DefaultClientConfigImpl();
//        config.loadProperties(this.name);
//        config.set(CommonClientConfigKey.ConnectTimeout, DEFAULT_CONNECT_TIMEOUT);
//        config.set(CommonClientConfigKey.ReadTimeout, DEFAULT_READ_TIMEOUT);
//        config.set(CommonClientConfigKey.GZipPayload, DEFAULT_GZIP_PAYLOAD);
//        return config;
//    }

}