package com.company.adminapi.filter;

/**
 * token 黑名单过滤器
 *
 * @author JQ棣
 * @email 897665787@qq.com
 * @date 2026/10/22 10:30
 * @Copyright Copyright (c) Speediance Inc. All Rights Reserved.
 **/

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.company.adminapi.service.OnlineUserService;
import com.company.framework.constant.CommonConstants;

/**
 * token 黑名单过滤器
 * <p>
 * 无状态 JWT 无法主动失效，强退时把 token 写入黑名单，这里统一拦截。
 */
@Component
@Order(CommonConstants.FilterOrdered.TOKEN + 1)
public class TokenBlacklistFilter extends OncePerRequestFilter {

	@Value("${token.name:Authorization}")
	private String headerToken;

	@Autowired
	private OnlineUserService onlineUserService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		String token = request.getHeader(headerToken);
		if (StringUtils.isNotBlank(token) && onlineUserService.isBlacklisted(token)) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.setCharacterEncoding(StandardCharsets.UTF_8.name());
			response.setContentType("application/json;charset=UTF-8");
			response.getWriter().write("{\"code\":\"401\",\"msg\":\"登录状态已失效，请重新登录\",\"data\":null}");
			return;
		}
		chain.doFilter(request, response);
	}
}
