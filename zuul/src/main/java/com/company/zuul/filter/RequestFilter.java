package com.company.zuul.filter;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingResponseWrapper;

import com.company.common.util.JsonUtil;
import com.company.zuul.filter.request.BodyReaderHttpServletRequestWrapper;
import com.company.zuul.util.IpUtil;
import com.company.zuul.util.WebUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * 请求参数日志打印
 */
@Slf4j
@Component
@Order(10)
public class RequestFilter extends OncePerRequestFilter {

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
		return false;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		String requestIp = IpUtil.getRequestIp(request);
		String headerStr = JsonUtil.toJsonString(WebUtil.getReqHeader(request));
		String method = request.getMethod();
		String requestURI = request.getRequestURI();

		long start = System.currentTimeMillis();
		try {
			String contentType = request.getContentType();
			String bodyStr = "{}";
			if (contentType != null && contentType.contains("application/json")) {
				BodyReaderHttpServletRequestWrapper requestWrapper = new BodyReaderHttpServletRequestWrapper(request);
				bodyStr = JsonUtil.toJsonString(JsonUtil.toJsonNode(requestWrapper.getBodyStr()));// 用json去掉有换行和空格
				request = requestWrapper;
			}

			String paramsStr = JsonUtil.toJsonString(WebUtil.getReqParam(request));

			// 包装缓存responseBody信息
			ContentCachingResponseWrapper cachingResponse = new ContentCachingResponseWrapper(response);
			chain.doFilter(request, cachingResponse);

			String responsePayload = getPayloadFromBuf(cachingResponse.getContentAsByteArray(),
					cachingResponse.getCharacterEncoding());

			// 把从response中读取过的内容重新放回response，否则客户端获取不到返回的数据
			cachingResponse.copyBodyToResponse();

			log.info("{} {} {} header:{},param:{},body:{},response:{},{}ms", method, requestIp, requestURI, headerStr,
					paramsStr, bodyStr, responsePayload, System.currentTimeMillis() - start);
		} catch (Exception e) {
			// 避免filter逻辑中的任何异常，直接转发请求
			log.error("{} {} {} header:{},{}ms,filter error", method, requestIp, requestURI, headerStr,
					System.currentTimeMillis() - start, e);
			chain.doFilter(request, response);
		}
	}

	/**
	 * 将bytep[]参数转换为字符串用于输出
	 * 
	 * @param buf
	 * @param characterEncoding
	 * @return
	 */
	protected String getPayloadFromBuf(byte[] buf, String characterEncoding) {
		String payload = "";
		if (buf.length > 0) {
			int length = buf.length;
			try {
				payload = new String(buf, 0, length, characterEncoding);
			} catch (UnsupportedEncodingException ex) {
				log.error(ex.getMessage(), ex);
			}
		}
		return payload;
	}
}