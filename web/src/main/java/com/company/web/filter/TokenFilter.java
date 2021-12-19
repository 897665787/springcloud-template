package com.company.web.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.company.framework.context.HttpContextUtil;
import com.company.web.filter.request.HeaderMapRequestWrapper;

/**
 * token解析
 */
@Component
@Order(30)
public class TokenFilter extends OncePerRequestFilter {
	private static final String HEADER_TOKEN = "x-token";

	@Value("${template.enable.access-control:true}")
	private Boolean enableAccessControl;

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
		return false;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		String token = request.getHeader(HEADER_TOKEN);
		String userId = TokenUtil.checkTokenAndGetSubject(token, enableAccessControl);

		if (StringUtils.isBlank(userId)) {
			chain.doFilter(request, response);
			return;
		}

		HeaderMapRequestWrapper headerRequest = new HeaderMapRequestWrapper(request);
		headerRequest.addHeader(HttpContextUtil.HEADER_CURRENT_USER_ID, userId);

		chain.doFilter(headerRequest, response);
	}
}