package com.company.framework.filter;

import com.company.framework.constant.CommonConstants;
import com.company.framework.context.Header2ContextUtil;
import com.company.framework.context.HttpContextUtil;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * 请求头放到上下文
 */
@Component
@Order(CommonConstants.FilterOrdered.USERCONTEXT)
public class Header2ContextFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        try {
            Map<String, String> headerMap = HttpContextUtil.httpContextHeaderThisRequest(request);
            Header2ContextUtil.setHeaderMap(headerMap);

            chain.doFilter(request, response);
        } finally {
            Header2ContextUtil.remove();
        }
    }
}
