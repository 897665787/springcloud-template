package com.company.adminapi.controller;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.company.framework.context.SpringContextUtil;
import com.company.token.TokenParams;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import com.company.adminapi.constants.Constants;
import com.company.adminapi.dto.OnlineSession;
import com.company.adminapi.service.LoginAttemptService;
import com.company.adminapi.service.OnlineUserService;
import com.company.adminapi.feign.client.SysDeptFeign;
import com.company.adminapi.feign.client.SysUserFeign;
import com.company.framework.context.HttpContextUtil;
import com.company.system.api.response.SysDeptResp;
import com.company.system.api.response.SysUserResp;
import com.company.adminapi.req.LoginReq;
import com.company.adminapi.resp.LoginResp;
import com.company.framework.context.HeaderContextUtil;
import com.company.framework.globalresponse.ExceptionUtil;
import com.company.framework.messagedriven.MessageSender;
import com.company.framework.messagedriven.constants.BroadcastConstants;
import com.company.adminapi.feign.client.SysUserPasswordFeign;
import com.company.system.api.response.SysUserPasswordTipsResp;
import com.company.token.TokenService;
import com.company.token.accesscontrol.annotation.RequireLogin;
import com.company.adminapi.feign.client.VerifyCodeFeign;
import com.company.tool.api.response.CaptchaResp;
import com.google.common.collect.Maps;

import cn.hutool.core.date.LocalDateTimeUtil;
import io.github.jqdi.easylogin.core.LoginClient;
import io.github.jqdi.easylogin.core.LoginParams;
import io.github.jqdi.easylogin.core.exception.LoginException;
import io.github.jqdi.easylogin.spring.boot.starter.LoginType;

/**
 * 账户（验证码、登录、登出）
 */
@RestController
@RequestMapping("/account")
public class AccountController {

	@Autowired
	private SysUserPasswordFeign sysUserPasswordFeign;
	@Autowired
	private TokenService tokenService;
	@Autowired
	private VerifyCodeFeign verifyCodeFeign;
	@Autowired
	private MessageSender messageSender;

	@Autowired
	@Qualifier(LoginType.USERNAME_PASSWORD_CODE)
	private LoginClient loginClient;
	@Autowired
	private OnlineUserService onlineUserService;
	@Autowired
	private SysDeptFeign sysDeptFeign;
	@Autowired
	private SysUserFeign sysUserFeign;
	@Autowired
	private LoginAttemptService loginAttemptService;

	@Value("${token.name}")
	private String headerToken;

	@Value("${token.prefix:}")
	private String tokenPrefix;

	@GetMapping(value = "/captcha")
	public CaptchaResp captcha() {
		return verifyCodeFeign.captcha(Constants.VerifyCodeType.ADMIN_LOGIN);
	}

	@PostMapping(value = "/login")
    public LoginResp login(@Valid @RequestBody LoginReq loginReq) {
        String account = loginReq.getAccount();
        if (loginAttemptService.isLocked(account)) {
            ExceptionUtil.throwException("账号已被锁定，请稍后再试或联系管理员解锁");
        }

        String sysUserIdStr = null;
        try {
            sysUserIdStr = loginClient.login(LoginParams.builder()
                .usernamePasswordCode(account, loginReq.getPassword(), loginReq.getUuid() + ":" + loginReq.getCode()).build());
        } catch (LoginException e) {
            loginAttemptService.recordFailure(account);
            ExceptionUtil.throwException(e.getMessage());
        }
        loginAttemptService.clear(account);
        Integer sysUserId = Integer.valueOf(sysUserIdStr);

        SysUserPasswordTipsResp sysUserPasswordTipsResp = sysUserPasswordFeign.getPasswordTipsBySysUserId(sysUserId);
        if (!sysUserPasswordTipsResp.getCanUse()) {
            ExceptionUtil.throwException(sysUserPasswordTipsResp.getPasswordTips());
        }

		String device = SpringContextUtil.getProperty("spring.application.name"); // 此次登录的客户端设备类型, 用于[同端互斥登录]时指定此次登录的设备类型
		TokenParams tokenParams = new TokenParams(String.valueOf(sysUserId), device);
		String tokenValue = tokenService.generate(tokenParams);
		if (StringUtils.isNoneBlank(tokenPrefix)) {
			tokenValue = tokenPrefix + " " + tokenValue;
		}

		// 记录在线会话，供「在线用户」查询与「强退」使用
		onlineUserService.save(buildOnlineSession(tokenValue, sysUserId, account));

		publishLoginEvent(sysUserId, device, account);

        LoginResp resp = new LoginResp();
        resp.setToken(tokenValue);
		resp.setTips(sysUserPasswordTipsResp.getPasswordTips());
        return resp;
	}

	/**
	 * 构建在线会话
	 */
	private OnlineSession buildOnlineSession(String token, Integer sysUserId, String account) {
		OnlineSession session = new OnlineSession();
		session.setTokenId(token);
		session.setSysUserId(sysUserId);
		session.setAccount(account);
		session.setIpaddr(HttpContextUtil.requestip());
		session.setLoginTime(LocalDateTime.now());

		SysUserResp sysUserResp = sysUserFeign.getById(sysUserId);
		if (sysUserResp != null && sysUserResp.getDeptId() != null) {
			SysDeptResp sysDeptResp = sysDeptFeign.query(sysUserResp.getDeptId());
			if (sysDeptResp != null) {
				session.setDeptName(sysDeptResp.getName());
			}
		}
		return session;
	}

	// 发布登录事件
	private void publishLoginEvent(Integer sysUserId, String device, String account) {
		Map<String, Object> params = Maps.newHashMap();
		params.put("device", device);
		params.put("sysUserId", sysUserId);
		params.put("account", account);
		params.put("loginTime", LocalDateTimeUtil.formatNormal(LocalDateTime.now()));
		params.put("httpContextHeader", HeaderContextUtil.httpContextHeader());
		messageSender.sendBroadcastMessage(params, BroadcastConstants.SYS_USER_LOGIN.EXCHANGE);
	}

	@RequireLogin
	@PostMapping(value = "/logout")
    public Map<String, String> logout(HttpServletRequest request) {
		String token = request.getHeader(headerToken);
		if (StringUtils.isBlank(token)) {
            return Collections.singletonMap("value", "登出成功");
		}

		tokenService.invalid(token);
		onlineUserService.remove(token);

        return Collections.singletonMap("value", "登出成功");
	}
}