package com.company.admin.springsecurity;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.company.admin.exception.ExceptionConsts;
import com.company.common.util.JsonUtil;
import com.google.common.collect.Maps;

/**
 * 系统用户访问拒绝处理器
 */
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException ade)
            throws IOException, ServletException {
        if (request.getHeader("X-Requested-With") == null) {
            String referer = request.getHeader("Referer");
            if (referer == null || referer.endsWith("/admin/error/403")) {
                if (referer == null) {
                    response.sendRedirect(request.getContextPath() + "/admin/error/403");
                }
                else {
                    response.sendRedirect(request.getContextPath() + "/admin/login");
                }
            }
            else {
                response.setHeader("Referer", referer);
                response.sendRedirect(request.getContextPath() + "/admin/error/403");
            }
        }
        else {
        	Map<String, Object> errorMap = Maps.newHashMap();
            errorMap.put("status", false);
            errorMap.put("code", ExceptionConsts.SEC_STAFF_ACCESS_DENY.getCode());
            errorMap.put("message", ExceptionConsts.SEC_STAFF_ACCESS_DENY.getMessage());
            String errorMsg = JsonUtil.toJsonString(errorMap);
            
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json;charset=utf-8");
            try {
                response.getWriter().write(errorMsg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
