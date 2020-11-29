package com.company.framework.filter;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.company.common.util.JsonUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * 请求参数日志打印
 */
@Slf4j
@Component
@Order(2)
public class RequestFilter extends OncePerRequestFilter {

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
		return false;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		long start = System.currentTimeMillis();
		chain.doFilter(request, response);
		log.info("{} {} {} {}", request.getMethod(), request.getRequestURI(), JsonUtil.toJsonString(getReqParam(request)), System.currentTimeMillis() - start);
	}

	/**
	 * 组装request中的参数
	 * 
	 * @param request
	 * @return
	 */
	private Map<String, Object> getReqParam(HttpServletRequest request) {
		Enumeration<String> parameterNames = request.getParameterNames();
		Map<String, Object> paramMap = new HashMap<>();
		while (parameterNames.hasMoreElements()) {
			String name = parameterNames.nextElement();
			paramMap.put(name, request.getParameter(name));
		}
		return paramMap;
	}

}