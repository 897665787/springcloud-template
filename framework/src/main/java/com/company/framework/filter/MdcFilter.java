package com.company.framework.filter;

import com.company.common.constant.CommonConstants;
import com.company.common.util.MdcUtil;
import com.company.framework.filter.request.HeaderMapRequestWrapper;
import com.company.framework.traceid.TraceIdManager;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 对客户端请求添加MDC
 */
@Component
@Order(CommonConstants.FilterOrdered.MDC)
public class MdcFilter extends OncePerRequestFilter {

	static {
		System.setProperty("log4j2.isThreadContextMapInheritable", "true");
	}

	@Autowired
	private TraceIdManager traceIdManager;

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
		return false;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		String traceId = request.getHeader(MdcUtil.UNIQUE_KEY);
		traceIdManager.put(traceId);
		if (StringUtils.isEmpty(traceId)) {
			HeaderMapRequestWrapper headerRequest = new HeaderMapRequestWrapper(request);
			headerRequest.addHeader(MdcUtil.UNIQUE_KEY, MdcUtil.get());
			request = headerRequest;
		}
		try {
			chain.doFilter(request, response);
		} finally {
			traceIdManager.remove();
		}
	}
}