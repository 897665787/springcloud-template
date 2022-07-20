package com.company.framework.aspect;

import java.io.IOException;
import java.util.Optional;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSocketFactory;

import feign.Client;
import feign.Request;
import feign.Response;
import lombok.extern.slf4j.Slf4j;

/**
 * 打印feign请求目标机器信息
 */
@Slf4j
public class FeignIPClient extends Client.Default {

	/**
	 * 默认构造方法
	 */
	public FeignIPClient() {
		this(null, null);
	}

	/**
	 * Null parameters imply platform defaults.
	 * 
	 * @param sslContextFactory
	 * @param hostnameVerifier
	 */
	public FeignIPClient(SSLSocketFactory sslContextFactory, HostnameVerifier hostnameVerifier) {
		super(sslContextFactory, hostnameVerifier);
	}

	@Override
	public Response execute(Request request, Request.Options options) throws IOException {
		long start = System.currentTimeMillis();
		Response response = super.execute(request, options);
		long end = System.currentTimeMillis();
		log.info("{}ms {} {} {}", end - start, request.method(), request.url(),
				Optional.ofNullable(request.body()).map(String::new).orElse(""));
		return response;
	}
}