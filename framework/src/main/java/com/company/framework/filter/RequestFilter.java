package com.company.framework.filter;

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

import com.company.common.constant.CommonConstants;
import com.company.common.util.JsonUtil;
import com.company.common.util.RegexUtil;
import com.company.framework.context.HttpContextUtil;
import com.company.framework.filter.request.BodyReaderHttpServletRequestWrapper;
import com.company.framework.util.IpUtil;
import com.company.framework.util.WebUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * 请求参数日志打印
 */
@Slf4j
@Component
@Order(CommonConstants.FilterOrdered.REQUEST)
public class RequestFilter extends OncePerRequestFilter {

	@Value("${template.requestFilter.ignoreLogPatterns:}")
	private String ignoreLogPatterns;
	@Value("${template.requestFilter.arrMaxLength:1000}")
	private int arrMaxLength;
	
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
			bodyStr = JsonUtil.toJsonStringReplaceProperties(JsonUtil.toJsonNode(requestWrapper.getBodyStr()), arrMaxLength);// 用json去掉有换行和空格
			request = requestWrapper;
		}
		
		String paramsStr = JsonUtil.toJsonStringReplaceProperties(WebUtil.getReqParam(request), arrMaxLength);
		String requestIp = IpUtil.getRequestIp(request);
		String headerStr = JsonUtil.toJsonStringReplaceProperties(HttpContextUtil.httpContextHeaderThisRequest(request), arrMaxLength);
		String method = request.getMethod();
		String requestURI = request.getRequestURI();

		log.info("{} {} {} header:{},param:{},body:{}", method, requestIp, requestURI, headerStr, paramsStr, bodyStr);

		chain.doFilter(request, response);

		log.info("{} {} {} header:{},param:{},body:{},{}ms", method, requestIp, requestURI, headerStr, paramsStr,
				bodyStr, System.currentTimeMillis() - start);
	}

}