package com.company.adminapi.filter;

/**
 * token 前缀兼容过滤器
 *
 * @author JQ棣
 * @email 897665787@qq.com
 * @date 2026/10/22 10:30
 * @Copyright Copyright (c) Speediance Inc. All Rights Reserved.
 **/

import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.company.framework.constant.CommonConstants;

/**
 * token 前缀兼容过滤器
 * <p>
 * 若依前端的上传、下载组件固定携带 "Bearer " 前缀，而后端 token 前缀为空，
 * 这里统一剥掉前缀，避免这些组件因 token 无法解析而返回 401。
 */
@Component
@Order(CommonConstants.FilterOrdered.TOKEN - 1)
public class TokenPrefixCompatFilter extends OncePerRequestFilter {

	// 前端固定携带的前缀
	private static final String BEARER_PREFIX = "Bearer ";

	@Value("${token.name:Authorization}")
	private String headerToken;

	@Value("${token.prefix:}")
	private String tokenPrefix;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		// 后端配置了 token 前缀时，前缀由 token 服务自行处理，无需兼容
		if (StringUtils.isNotBlank(tokenPrefix)) {
			chain.doFilter(request, response);
			return;
		}
		String token = request.getHeader(headerToken);
		if (StringUtils.isBlank(token) || !StringUtils.startsWithIgnoreCase(token, BEARER_PREFIX.trim())) {
			chain.doFilter(request, response);
			return;
		}
		String newToken = StringUtils.removeStartIgnoreCase(token, BEARER_PREFIX.trim()).trim();
		chain.doFilter(new TokenHeaderRequestWrapper(request, headerToken, newToken), response);
	}

	/**
	 * 重写 token 请求头
	 */
	private static class TokenHeaderRequestWrapper extends HttpServletRequestWrapper {

		// 请求头名称
		private final String headerName;
		// 请求头值
		private final String headerValue;

		TokenHeaderRequestWrapper(HttpServletRequest request, String headerName, String headerValue) {
			super(request);
			this.headerName = headerName;
			this.headerValue = headerValue;
		}

		@Override
		public String getHeader(String name) {
			if (headerName.equalsIgnoreCase(name)) {
				return headerValue;
			}
			return super.getHeader(name);
		}

		@Override
		public Enumeration<String> getHeaders(String name) {
			if (headerName.equalsIgnoreCase(name)) {
				return Collections.enumeration(Collections.singletonList(headerValue));
			}
			return super.getHeaders(name);
		}

		@Override
		public Enumeration<String> getHeaderNames() {
			List<String> names = Collections.list(super.getHeaderNames());
			return Collections.enumeration(names);
		}
	}
}
