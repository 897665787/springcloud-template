package com.company.admin.springsecurity;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import com.company.admin.exception.ExceptionConsts;
import com.company.common.util.JsonUtil;
import com.google.common.collect.Maps;

/**
 * 系统用户登录失败处理器
 */
@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        String exceptionMsg = exception.getMessage();
        Integer code = ExceptionConsts.FAILURE.getCode();
        String msg = ExceptionConsts.FAILURE.getMessage();

        if (exception instanceof UsernameNotFoundException) {
            if (exception.getMessage().equals(ExceptionConsts.FAILURE.getMessage())) {
                code = ExceptionConsts.FAILURE.getCode();
                msg = ExceptionConsts.FAILURE.getMessage();
            }
            else {
                code = ExceptionConsts.SEC_STAFF_NOT_EXIST.getCode();
                msg = ExceptionConsts.SEC_STAFF_NOT_EXIST.getMessage();
            }
        }
        else if (exception instanceof BadCredentialsException) {
            code = ExceptionConsts.SEC_STAFF_PASSWORD_ERROR.getCode();
            msg = ExceptionConsts.SEC_STAFF_PASSWORD_ERROR.getMessage();
        }
        else if (exception instanceof InternalAuthenticationServiceException) {
            code = ExceptionConsts.SEC_STAFF_NOT_ENABLE.getCode();
            msg = ExceptionConsts.SEC_STAFF_NOT_ENABLE.getMessage();
        }
        else if (exceptionMsg.contains("principal exceeded")) {
            code = ExceptionConsts.SEC_STAFF_CONCURRENT_BEYOND.getCode();
            msg = ExceptionConsts.SEC_STAFF_CONCURRENT_BEYOND.getMessage();
        }
        
        Map<String, Object> errorMap = Maps.newHashMap();
        errorMap.put("status", false);
        errorMap.put("code", code);
        errorMap.put("message", msg);
        String errorMsg = JsonUtil.toJsonString(errorMap);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(errorMsg);
    }
}
