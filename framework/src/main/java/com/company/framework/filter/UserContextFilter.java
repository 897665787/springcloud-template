package com.company.framework.filter;

import com.company.framework.constant.CommonConstants;
import com.company.framework.constant.HeaderConstants;
import com.company.framework.context.UserContextUtil;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 将请求头用户ID放入用户上下文中
 */
@Component
@Order(CommonConstants.FilterOrdered.MDC)
public class UserContextFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        String userId = request.getHeader(HeaderConstants.HEADER_CURRENT_USER_ID);
        UserContextUtil.setCurrentUserId(userId);
        try {
            chain.doFilter(request, response);
        } finally {
            UserContextUtil.remove();
        }
    }
}
