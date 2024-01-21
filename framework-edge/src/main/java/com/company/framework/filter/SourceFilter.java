package com.company.framework.filter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.company.common.constant.CommonConstants;
import com.company.framework.context.HttpContextUtil;
import com.company.framework.filter.request.HeaderMapRequestWrapper;

/**
 * Http上下文公共请求信息设置到header
 * （只有接收外部请求的服务才会用到，内部请求的服务通过FeignHeaderInterceptor已经直接放到header）
 */
@Component
@Order(CommonConstants.FilterOrdered.SOURCE)
public class SourceFilter extends OncePerRequestFilter {

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
		return false;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HeaderMapRequestWrapper headerRequest = new HeaderMapRequestWrapper(request);
		
		String source = request.getHeader(HttpContextUtil.HEADER_SOURCE);
		if (StringUtils.isBlank(source)) {
			source = request.getParameter(HttpContextUtil.HEADER_SOURCE);
		}

		if (StringUtils.isBlank(source)) {
			chain.doFilter(headerRequest, response);
			return;
		}

		String deviceid = request.getHeader(HttpContextUtil.HEADER_DEVICEID);
		if (StringUtils.isBlank(deviceid)) {
			deviceid = request.getParameter(HttpContextUtil.HEADER_DEVICEID);
		}
		
		if (StringUtils.isBlank(deviceid)) {
			chain.doFilter(headerRequest, response);
			return;
		}

		// source、deviceid、时间 发送到MQ异步记录来源
		LocalDateTime now = LocalDateTime.now();
		now.format(DateTimeFormatter.ofPattern(""));

		chain.doFilter(headerRequest, response);
	}
}