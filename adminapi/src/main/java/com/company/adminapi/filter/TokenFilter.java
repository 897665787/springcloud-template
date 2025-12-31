package com.company.adminapi.filter;

import com.company.token.TokenService;
import com.company.framework.constant.CommonConstants;
import com.company.framework.constant.HeaderConstants;
import com.company.framework.filter.request.HeaderMapRequestWrapper;
import com.company.token.util.TokenValueUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * token解析，把token转换为USER_ID
 */
@Component
@Order(CommonConstants.FilterOrdered.TOKEN)
@RequiredArgsConstructor
public class TokenFilter extends OncePerRequestFilter {

	private final TokenService tokenService;

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

		String userId = StringUtils.EMPTY;// 注：为了防止直接在header设置用户ID，绕过认证，null情况下要设置用户ID为空串
		if (StringUtils.isNotBlank(token)) {
			userId = tokenService.checkAndGet(token);
			if (userId == null) {
				userId = StringUtils.EMPTY;// 注：为了防止直接在header设置用户ID，绕过认证，null情况下要设置用户ID为空串
			}
		}
		headerRequest.addHeader(HeaderConstants.HEADER_CURRENT_USER_ID, userId);
		chain.doFilter(headerRequest, response);
	}
}
