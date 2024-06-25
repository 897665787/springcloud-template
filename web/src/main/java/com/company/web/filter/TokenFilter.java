package com.company.web.filter;

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

import com.company.common.constant.CommonConstants;
import com.company.common.constant.HeaderConstants;
import com.company.framework.filter.request.HeaderMapRequestWrapper;
import com.company.web.token.TokenService;
import com.company.web.util.TokenValueUtil;

/**
 * token解析，把token转换为USER_ID
 */
@Component
@Order(CommonConstants.FilterOrdered.TOKEN)
public class TokenFilter extends OncePerRequestFilter {
	
	@Autowired
	private TokenService tokenService;
	
	@Value("${token.name}")
	private String headerToken;
	
	@Value("${token.prefix:}")
	private String tokenPrefix;

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
		return false;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		String token = request.getHeader(headerToken);
		token = TokenValueUtil.fixToken(tokenPrefix, token);
		HeaderMapRequestWrapper headerRequest = new HeaderMapRequestWrapper(request);
		if (StringUtils.isBlank(token)) {
			// 注：为了防止直接在header设置用户ID，绕过认证，要设置用户ID为空
			headerRequest.addHeader(HeaderConstants.HEADER_CURRENT_USER_ID, StringUtils.EMPTY);
			chain.doFilter(headerRequest, response);
			return;
		}
		
		String userId = tokenService.checkAndGet(token);
		if (StringUtils.isBlank(userId)) {
			// 注：为了防止直接在header设置用户ID，绕过认证，要设置用户ID为空
			headerRequest.addHeader(HeaderConstants.HEADER_CURRENT_USER_ID, StringUtils.EMPTY);
			chain.doFilter(headerRequest, response);
			return;
		}

		headerRequest.addHeader(HeaderConstants.HEADER_CURRENT_USER_ID, userId);
		chain.doFilter(headerRequest, response);
	}
}