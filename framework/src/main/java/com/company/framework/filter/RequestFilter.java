package com.company.framework.filter;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.company.common.util.JsonUtil;
import com.company.common.util.RegexUtil;
import com.company.framework.context.HttpContextUtil;
import com.company.framework.filter.request.BodyReaderHttpServletRequestWrapper;
import com.company.framework.util.IpUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * 请求参数日志打印
 */
@Slf4j
@Component
@Order(10)
public class RequestFilter extends OncePerRequestFilter {

	@Value("${template.requestFilter.ignoreLogPatterns:}")
	private String ignoreLogPatterns;
	
	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
		if (StringUtils.isBlank(ignoreLogPatterns)) {
			return false;
		}
		
		String requestURI = request.getRequestURI();
		String[] ignoreLogPatternss = StringUtils.split(ignoreLogPatterns, ",");
		for (String ignoreLogPattern : ignoreLogPatternss) {
			boolean match = RegexUtil.match(ignoreLogPattern, requestURI);
			if (match) {
				// 匹配任意一个表达式就不打印日志
				return true;
			}
		}
		return false;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		long start = System.currentTimeMillis();
		String contentType = request.getContentType();
		String bodyStr = "{}";
		if (contentType != null && contentType.contains("application/json")) {
			BodyReaderHttpServletRequestWrapper requestWrapper = new BodyReaderHttpServletRequestWrapper(request);
			bodyStr = JsonUtil.toJsonString(JsonUtil.toJsonNode(requestWrapper.getBodyStr()));// 用json去掉有换行和空格
			request = requestWrapper;
		}
		
		String paramsStr = JsonUtil.toJsonString(getReqParam(request));
		String requestIp = IpUtil.getRequestIp(request);
		String headerStr = JsonUtil.toJsonString(HttpContextUtil.httpContextHeaderThisRequest(request));
		String method = request.getMethod();
		String requestURI = request.getRequestURI();

		log.info("{} {} {} header:{},param:{},body:{}", method, requestIp, requestURI, headerStr, paramsStr, bodyStr);

		chain.doFilter(request, response);

		log.info("{} {} {} header:{},param:{},body:{},{}ms", method, requestIp, requestURI, headerStr, paramsStr,
				bodyStr, System.currentTimeMillis() - start);
	}

	/**
	 * 组装request中的参数
	 * 
	 * <pre>
	 * 以下场景都能通过request.getParameterNames获取参数
	 * 1.参数跟在url后面
	 * 2.POST form-data
	 * 3.POST x-www-form-urlencoded
	 * </pre>
	 * 
	 * @param request
	 * @return
	 */
	private Map<String, Object> getReqParam(HttpServletRequest request) {
		Enumeration<String> parameterNames = request.getParameterNames();
		Map<String, Object> paramMap = new HashMap<>();
		while (parameterNames.hasMoreElements()) {
			String name = parameterNames.nextElement();
			paramMap.put(name, request.getParameter(name));
		}
		return paramMap;
	}

}