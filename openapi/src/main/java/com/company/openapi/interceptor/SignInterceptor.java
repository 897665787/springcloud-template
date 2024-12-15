package com.company.openapi.interceptor;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.company.common.api.Result;
import com.company.common.util.JsonUtil;
import com.company.framework.cache.ICache;
import com.company.framework.filter.request.BodyReaderHttpServletRequestWrapper;
import com.company.framework.util.WebUtil;
import com.company.openapi.annotation.NoSign;
import com.company.openapi.config.SignConfiguration;
import com.company.openapi.util.SignUtil;

@Component
@ConditionalOnProperty(prefix = "sign", name = "check", havingValue = "true", matchIfMissing = true)
public class SignInterceptor extends HandlerInterceptorAdapter {
	
	@Autowired
	private SignConfiguration signConfiguration;
	@Autowired
	private ICache cache;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		if (!(handler instanceof HandlerMethod)) {
			return true;
		}

		HandlerMethod handlerMethod = (HandlerMethod) handler;

		Method method = handlerMethod.getMethod();
		
		NoSign methodAnnotation = AnnotationUtils.findAnnotation(method, NoSign.class);
		NoSign classAnnotation = AnnotationUtils.findAnnotation(method.getDeclaringClass(), NoSign.class);
		if (methodAnnotation != null || classAnnotation != null) {
			// 方法或类上打注解NoSign
			return true;
		}
		
		// 检查时效性
		String timestamp = request.getHeader("timestamp");// 加入timestamp（时间戳），10分钟内数据有效
		if (StringUtils.isBlank(timestamp)) {
			writeError(response, "请求已过期");
			return false;
		}
		long timestampLong = Long.parseLong(timestamp);
		long now = System.currentTimeMillis();
		if (now - timestampLong > signConfiguration.getReqValidSeconds() * 1000) {
			writeError(response, "请求已过期");
			return false;
		}

		// 检查appid是否正确
		String appid = request.getHeader("appid");// 线下分配appid和appsecret，针对不同的调用方分配不同的appid和appsecret
		if (StringUtils.isBlank(appid)) {
			writeError(response, "appid错误");
			return false;
		}
		
		String appsecret = signConfiguration.getAppsecret(appid);
//		String appsecret = openAccessAccountFeign.getAppKeyByAppid(appid).dataOrThrow();// appsecret也可以保存到数据库
		if (StringUtils.isBlank(appsecret)) {
			writeError(response, "appid错误");
			return false;
		}

		String noncestr = request.getHeader("noncestr");// 加入流水号noncestr（防止重复提交），至少为10位。针对查询接口，流水号只用于日志落地，便于后期日志核查。
														// 针对办理类接口需校验流水号在有效期内的唯一性，以避免重复请求。
		if (signConfiguration.nonceValid()) {
			String key = String.format("nonce:%s", noncestr);
			String value = cache.get(key);
			if (StringUtils.isNotBlank(value)) {
				writeError(response, "请求重复");
				return false;
			}
			cache.set(key, "1", signConfiguration.getReqValidSeconds(), TimeUnit.SECONDS);
		}
		
		String sign = request.getHeader("sign"); // 签名

		String contentType = request.getContentType();
		String bodyStr = "";
		if (contentType != null && contentType.contains("application/json")) {
			BodyReaderHttpServletRequestWrapper requestWrapper = new BodyReaderHttpServletRequestWrapper(request);
			bodyStr = JsonUtil.toJsonString(JsonUtil.toJsonNode(requestWrapper.getBodyStr()));// 用json去掉有换行和空格
		}

		Map<String, String> reqParam = WebUtil.getReqParam(request);
		Map<String, Object> reqParamObject = reqParam.entrySet().stream()
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
		
		String sign4md5 = SignUtil.generate(appid, timestampLong, noncestr, reqParamObject , bodyStr, appsecret);
		if (!sign4md5.equals(sign)) {
			writeError(response, "签名错误");
			return false;
		}
		return true;
	}

	private void writeError(HttpServletResponse response, String message) throws IOException {
		response.setContentType("application/json");
		response.setCharacterEncoding("utf-8");
		PrintWriter writer = response.getWriter();
		writer.write(JsonUtil.toJsonString(Result.fail(message)));
	}
}
