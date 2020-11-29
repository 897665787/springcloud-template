package com.company.framework.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * 对客户端请求添加MDC
 */
@Component
@Order(1)
public class MdcFilter extends OncePerRequestFilter {

	static {
		System.setProperty("log4j2.isThreadContextMapInheritable", "true");
	}

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
		return false;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		String traceId = request.getHeader(MdcUtil.UNIQUE_KEY);
		MdcUtil.put(traceId);
		try {
			chain.doFilter(request, response);
		} finally {
			MdcUtil.remove();
		}
	}
}