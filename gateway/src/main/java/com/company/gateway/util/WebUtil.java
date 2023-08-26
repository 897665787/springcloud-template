package com.company.gateway.util;

import java.util.Map;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.MultiValueMap;

public class WebUtil {

	private WebUtil() {
	}

	/**
	 * 组装request中的参数
	 * 
	 * <pre>
	 * 以下场景都能通过request.getParameterNames获取参数
	 * 1.参数跟在url后面
	 * 2.POST form-data
	 * 3.POST x-www-form-urlencoded
	 * </pre>
	 * 
	 * @param request
	 * @return
	 */
	public static Map<String, String> getReqParam(ServerHttpRequest request) {
		MultiValueMap<String, String> queryParams = request.getQueryParams();
		Map<String, String> paramMap = queryParams.toSingleValueMap();
		return paramMap;
	}
}