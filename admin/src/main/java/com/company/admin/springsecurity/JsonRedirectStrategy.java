package com.company.admin.springsecurity;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.web.RedirectStrategy;

import com.company.common.api.ResultCode;
import com.company.common.util.JsonUtil;
import com.google.common.collect.Maps;

public class JsonRedirectStrategy implements RedirectStrategy {
    
	@Override
	public void sendRedirect(HttpServletRequest request, HttpServletResponse response, String url) throws IOException {
		Map<String, Object> successMap = Maps.newHashMap();
		successMap.put("status", true);
		successMap.put("code", ResultCode.SUCCESS.getCode());
		successMap.put("url", url);
        String successMsg = JsonUtil.toJsonString(successMap);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(successMsg);
	}
}
