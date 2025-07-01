package com.company.admin.springsecurity;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import com.company.common.api.ResultCode;
import com.company.framework.util.JsonUtil;
import com.google.common.collect.Maps;

/**
 * 系统用户登出成功处理器
 */
@Component
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response,
                                Authentication authentication) throws IOException, ServletException {
    	Map<String, Object> successMap = Maps.newHashMap();
		successMap.put("status", true);
		successMap.put("code", ResultCode.SUCCESS.getCode());
        String successMsg = JsonUtil.toJsonString(successMap);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(successMsg);
    }
}
