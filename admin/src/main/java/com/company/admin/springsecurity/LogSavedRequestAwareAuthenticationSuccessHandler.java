package com.company.admin.springsecurity;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import com.company.admin.entity.security.SecStaff;
import com.company.admin.entity.security.SecStaffLog;
import com.company.admin.service.security.SecStaffLogService;
import com.company.admin.service.security.SecStaffService;
import com.company.framework.util.IpUtil;

/**
 * 系统用户登录成功处理器
 */
public class LogSavedRequestAwareAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Autowired
    private SecStaffLogService secStaffLogService;
    @Autowired
    private SecStaffService secStaffService;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws ServletException, IOException {
		super.onAuthenticationSuccess(request, response, authentication);
		
        //记录成功登录日志
        SecStaffLog latest = new SecStaffLog();
        SecStaff secStaff = secStaffService.getByUsername(new SecStaff(((User) authentication.getPrincipal()).getUsername()));
		latest.setStaff(secStaff);
        latest.setIp(IpUtil.getRequestIp(request));
        latest.setTime(new Date());
        latest.setOperation("登录系统");
        latest.setStatus(1);
        latest.setParameters("");
        latest.setResult("");
        secStaffLogService.save(latest);
	}
    
}
