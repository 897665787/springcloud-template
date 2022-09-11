package com.company.framework.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.company.framework.context.HttpContextUtil;
import com.company.framework.filter.request.HeaderMapRequestWrapper;

/**
 * Http上下文公共请求信息设置到header
 * （只有接收外部请求的服务才会用到，内部请求的服务通过FeignHeaderInterceptor已经直接放到header）
 */
@Component
@Order(5)
public class HttpContextFilter extends OncePerRequestFilter {

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
		return false;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HeaderMapRequestWrapper headerRequest = new HeaderMapRequestWrapper(request);
		
		// 有些请求的公共信息没有放在header，而是跟在url后面
		String platform = request.getHeader(HttpContextUtil.HEADER_PLATFORM);
		if (StringUtils.isBlank(platform)) {
			platform = request.getParameter(HttpContextUtil.HEADER_PLATFORM);
			if (StringUtils.isNotBlank(platform)) {
				headerRequest.addHeader(HttpContextUtil.HEADER_PLATFORM, platform);
			}
		}

		String operator = request.getHeader(HttpContextUtil.HEADER_OPERATOR);
		if (StringUtils.isBlank(operator)) {
			operator = request.getParameter(HttpContextUtil.HEADER_OPERATOR);
			if (StringUtils.isNotBlank(operator)) {
				headerRequest.addHeader(HttpContextUtil.HEADER_OPERATOR, operator);
			}
		}

		String version = request.getHeader(HttpContextUtil.HEADER_VERSION);
		if (StringUtils.isBlank(version)) {
			version = request.getParameter(HttpContextUtil.HEADER_VERSION);
			if (StringUtils.isNotBlank(version)) {
				headerRequest.addHeader(HttpContextUtil.HEADER_VERSION, version);
			}
		}

		String deviceid = request.getHeader(HttpContextUtil.HEADER_DEVICEID);
		if (StringUtils.isBlank(deviceid)) {
			deviceid = request.getParameter(HttpContextUtil.HEADER_DEVICEID);
			if (StringUtils.isNotBlank(deviceid)) {
				headerRequest.addHeader(HttpContextUtil.HEADER_DEVICEID, deviceid);
			}
		}

		String source = request.getHeader(HttpContextUtil.HEADER_SOURCE);
		if (StringUtils.isBlank(source)) {
			source = request.getParameter(HttpContextUtil.HEADER_SOURCE);
			if (StringUtils.isNotBlank(source)) {
				headerRequest.addHeader(HttpContextUtil.HEADER_SOURCE, source);
			}
		}
		
		chain.doFilter(headerRequest, response);
	}
}