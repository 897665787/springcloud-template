package com.company.framework.autoconfigure;

import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateAutoConfiguration {

	@Bean
	public ClientHttpRequestFactory httpRequestFactory() {
		return new HttpComponentsClientHttpRequestFactory(httpClient());
	}

	// 启用负载，注册在eureka服务上的实例有效 需要 通过服务名访问
	@LoadBalanced
	@Bean("restTemplate")
	public RestTemplate restTemplate() {
		return new RestTemplate(httpRequestFactory());
	}

	@Bean
	public HttpClient httpClient() {
		Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory> create()
				.register("http", PlainConnectionSocketFactory.getSocketFactory())
				.register("https", SSLConnectionSocketFactory.getSocketFactory()).build();
		PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(registry);
		// 总连接数
		connectionManager.setMaxTotal(2000);
		// 同路由的并发数
		connectionManager.setDefaultMaxPerRoute(1500);
		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(20000).setConnectTimeout(20000)
				// 连接不够用的等待时间，不宜过长，必须设置，比如连接不够用时，时间过长将是灾难性的
				.setConnectionRequestTimeout(8000).build();

		return HttpClients.custom().setDefaultRequestConfig(requestConfig).setConnectionManager(connectionManager)
				// 重试次数，默认是3次，没有开启
				// .setRetryHandler(new DefaultHttpRequestRetryHandler(3, true))
				.build();
	}
}