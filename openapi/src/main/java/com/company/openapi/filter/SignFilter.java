package com.company.openapi.filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.company.common.api.Result;
import com.company.common.util.JsonUtil;
import com.company.framework.filter.request.BodyReaderHttpServletRequestWrapper;
import com.company.openapi.util.SignUtil;
import com.company.user.api.feign.OpenAccessAccountFeign;

/**
 * 签名过滤器
 */
@Component
@Order(20)
public class SignFilter extends OncePerRequestFilter {

	@Autowired
	private OpenAccessAccountFeign openAccessAccountFeign;
	
	@Value("${template.sign.check:true}")
	private boolean checkSign;
	
	@Value("${template.sign.reqValidSeconds:60}")
	private long reqValidSeconds;
	
	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
		return false;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		// 是否检查签名
		if (!checkSign) {
			chain.doFilter(request, response);
			return;
		}
		
		// 检查时效性
		String timestamp = request.getHeader("timestamp");// 加入timestamp（时间戳），10分钟内数据有效
		if (StringUtils.isBlank(timestamp)) {
			writeError(response, "请求已过期");
			return;
		}
		long timestampLong = Long.parseLong(timestamp);
		long now = System.currentTimeMillis();
		if (now - timestampLong > reqValidSeconds * 1000) {
			writeError(response, "请求已过期");
			return;
		}

		// 检查appid是否正确
		String appid = request.getHeader("appid");// 线下分配appid和appsecret，针对不同的调用方分配不同的appid和appsecret
		if (StringUtils.isBlank(appid)) {
			writeError(response, "appid错误");
			return;
		}
		
		String appsecret = openAccessAccountFeign.getAppKeyByAppid(appid).dataOrThrow();
		if (StringUtils.isBlank(appsecret)) {
			writeError(response, "appid错误");
			return;
		}
		
		String noncestr = request.getHeader("noncestr");// 加入流水号noncestr（防止重复提交），至少为10位。针对查询接口，流水号只用于日志落地，便于后期日志核查。
														// 针对办理类接口需校验流水号在有效期内的唯一性，以避免重复请求。
		String sign = request.getHeader("sign"); // 签名

		String contentType = request.getContentType();
		String bodyStr = "";
		if (contentType != null && contentType.contains("application/json")) {
			BodyReaderHttpServletRequestWrapper requestWrapper = new BodyReaderHttpServletRequestWrapper(request);
			bodyStr = JsonUtil.toJsonString(JsonUtil.toJsonNode(requestWrapper.getBodyStr()));// 用json去掉有换行和空格
			request = requestWrapper;
		}

		Map<String, Object> reqParam = getReqParam(request);
		
		String sign4md5 = SignUtil.generate(appid, timestampLong, noncestr, reqParam , bodyStr, appsecret);
		if (!sign4md5.equals(sign)) {
			writeError(response, "签名错误");
			return;
		}
		
		chain.doFilter(request, response);
	}

	private void writeError(HttpServletResponse response, String message) throws IOException {
		response.setContentType("application/json");
		response.setCharacterEncoding("utf-8");
		PrintWriter writer = response.getWriter();
		writer.write(JsonUtil.toJsonString(Result.fail(message)));
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