package com.company.framework.developer.policy.context;

import com.company.common.constant.CommonConstants;
import com.company.common.constant.HeaderConstants;
import org.springframework.core.annotation.Order;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 获取请求头中的开发者ID，并将其存入上下文中，供后续使用。
 */
@Order(0)
public class DeveloperFilter extends OncePerRequestFilter {

    @Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
		String userId = request.getHeader(HeaderConstants.HEADER_CURRENT_USER_ID);
		DeveloperContext.set(userId);
		try {
			chain.doFilter(request, response);
		} finally {
			DeveloperContext.remove();
		}
	}
}