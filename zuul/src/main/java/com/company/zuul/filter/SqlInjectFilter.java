package com.company.zuul.filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.MessageFormat;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.company.common.api.Result;
import com.company.common.util.JsonUtil;
import com.company.zuul.filter.request.BodyReaderHttpServletRequestWrapper;
import com.company.zuul.util.WebUtil;
import com.fasterxml.jackson.databind.JsonNode;

import lombok.extern.slf4j.Slf4j;

/**
 * SQL注入过滤
 */
@Slf4j
@Order(20)
@Component
public class SqlInjectFilter extends OncePerRequestFilter {
	
	@Value("${template.sqlInjectFilter.keywords:}")
	private String[] sqlInjectKeywords;

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
		return sqlInjectKeywords == null || sqlInjectKeywords.length == 0;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		try {
			String contentType = request.getContentType();
			if (contentType != null && contentType.contains("application/json")) {
				BodyReaderHttpServletRequestWrapper requestWrapper = new BodyReaderHttpServletRequestWrapper(request);
				JsonNode bodyNode = JsonUtil.toJsonNode(requestWrapper.getBodyStr());
				Iterator<Entry<String, JsonNode>> fields = bodyNode.fields();
				while (fields.hasNext()) {
					Entry<String, JsonNode> entry = fields.next();
					String value = Optional.ofNullable(entry.getValue()).map(JsonNode::asText).orElse(null);
					if (StringUtils.isBlank(value)) {
						continue;
					}
					for (String keyword : sqlInjectKeywords) {
						if (StringUtils.containsIgnoreCase(value, keyword)) {
							String key = entry.getKey();
							log.warn("参数‘{}’存在sql注入风险:{}", key, value);
							writeError(response, MessageFormat.format("参数‘{0}’不合法", key));
							return;
						}
					}
				}

				request = requestWrapper;
			}

			Map<String, String> reqParamMap = WebUtil.getReqParam(request);
			Set<Entry<String, String>> entrySet = reqParamMap.entrySet();
			for (Entry<String, String> entry : entrySet) {
				String value = entry.getValue();
				if (StringUtils.isBlank(value)) {
					continue;
				}
				for (String keyword : sqlInjectKeywords) {
					if (StringUtils.containsIgnoreCase(value, keyword)) {
						String key = entry.getKey();
						log.warn("参数‘{}’存在sql注入风险:{}", key, value);
						writeError(response, MessageFormat.format("参数‘{0}’不合法", key));
						return;
					}
				}
			}

			chain.doFilter(request, response);
		} catch (Exception e) {
			// 避免filter逻辑中的任何异常，直接转发请求
			log.error("filter error", e);
			chain.doFilter(request, response);
		}
	}

	private void writeError(HttpServletResponse response, String message) throws IOException {
		response.setContentType("application/json");
		response.setCharacterEncoding("utf-8");
		PrintWriter writer = response.getWriter();
		writer.write(JsonUtil.toJsonString(Result.fail(message)));
	}
}