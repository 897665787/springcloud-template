package com.company.framework.threadpool.tomcat;

import org.springframework.boot.web.embedded.tomcat.ConfigurableTomcatWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.core.Ordered;

/**
 * 修改tomcat内置线程池，使用自定义的线程池
 */
public class ThreadPoolTomcatWebServerFactoryCustomizer
		implements WebServerFactoryCustomizer<ConfigurableTomcatWebServerFactory>, Ordered {

	@Override
	public int getOrder() {
		// 注：源码中的WebServerFactoryCustomizer实现类order默认为1，设置大于1将优先级调至最低，否则CustomTomcatConnectorCustomizer中protocol获取配置都是默认值，而不是修改后的值
		return Integer.MAX_VALUE;
	}

	@Override
	public void customize(ConfigurableTomcatWebServerFactory factory) {
		factory.addConnectorCustomizers(new CustomTomcatConnectorCustomizer());
	}

}