package com.company.zuul.filter;

import java.io.IOException;

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

import com.company.framework.context.HttpContextUtil;
import com.company.framework.filter.request.HeaderMapRequestWrapper;
import com.company.zuul.token.TokenService;

/**
 * token解析，把token转换为USER_ID
 */
@Component
@Order(30)
public class TokenFilter extends OncePerRequestFilter {
	
	@Autowired
	private TokenService tokenService;
	
	@Value("${token.name}")
	private String headerToken;

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
		return false;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		String token = request.getHeader(headerToken);
		if (StringUtils.isBlank(token)) {
			chain.doFilter(request, response);
			return;
		}
		
		String userId = tokenService.checkAndGet(token);
		if (StringUtils.isBlank(userId)) {
			chain.doFilter(request, response);
			return;
		}

		HeaderMapRequestWrapper headerRequest = new HeaderMapRequestWrapper(request);
		headerRequest.addHeader(HttpContextUtil.HEADER_CURRENT_USER_ID, userId);
		
		chain.doFilter(headerRequest, response);
	}
}