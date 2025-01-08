package com.company.adminapi.controller;

import java.time.LocalDateTime;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.company.adminapi.constants.Constants;
import com.company.adminapi.req.LoginReq;
import com.company.adminapi.resp.LoginResp;
import com.company.adminapi.token.TokenService;
import com.company.adminapi.util.PassWordUtil;
import com.company.adminapi.util.TokenValueUtil;
import com.company.common.api.Result;
import com.company.framework.amqp.MessageSender;
import com.company.framework.amqp.constants.FanoutConstants;
import com.company.framework.annotation.RequireLogin;
import com.company.framework.context.HttpContextUtil;
import com.company.tool.api.feign.VerifyCodeFeign;
import com.company.tool.api.response.CaptchaResp;
import com.google.common.collect.Maps;
import com.company.system.api.feign.SysUserFeign;
import com.company.system.api.feign.SysUserPasswordFeign;
import com.company.system.api.response.SysUserPasswordResp;
import com.company.system.api.response.SysUserResp;

import cn.hutool.core.date.LocalDateTimeUtil;

/**
 * 账户（验证码、登录、登出）
 */
@RestController
@RequestMapping("/account")
public class AccountController {

	@Autowired
	private SysUserFeign sysUserFeign;
	@Autowired
	private SysUserPasswordFeign sysUserPasswordFeign;
	@Autowired
	private TokenService tokenService;
	@Autowired
	private VerifyCodeFeign verifyCodeFeign;
	@Autowired
	private MessageSender messageSender;
	
	@Value("${token.name}")
	private String headerToken;

	@Value("${token.prefix:}")
	private String tokenPrefix;

	@GetMapping(value = "/captcha")
	public Result<CaptchaResp> captcha() {
		return verifyCodeFeign.captcha(Constants.VerifyCodeType.ADMIN_LOGIN);
	}

	@PostMapping(value = "/login")
	public Result<LoginResp> login(@Valid @RequestBody LoginReq loginReq) {
		String account = loginReq.getAccount();
		SysUserResp sysUserResp = sysUserFeign.getByAccount(account).dataOrThrow();
		if (sysUserResp == null) {
			return Result.fail("账号不存在");
		}

		if (!"ON".equalsIgnoreCase(sysUserResp.getStatus())) {
			return Result.fail("账号已停用");
		}

		String password = loginReq.getPassword();
		String md5Password = PassWordUtil.md5(password);
		SysUserPasswordResp sysUserPasswordResp = sysUserPasswordFeign.getBySysUserId(sysUserResp.getId())
				.dataOrThrow();
		if (!sysUserPasswordResp.getCanUse()) {
			return Result.fail(sysUserPasswordResp.getPasswordTips());
		}
		
		if (!md5Password.equals(sysUserPasswordResp.getPassword())) {
			return Result.fail("密码错误");
		}

		Boolean verifyPass = verifyCodeFeign
				.verify(Constants.VerifyCodeType.ADMIN_LOGIN, loginReq.getUuid(), loginReq.getCode()).dataOrThrow();
		if (!verifyPass) {
			return Result.fail("验证码错误");
		}

		Integer sysUserId = sysUserResp.getId();

		String device = "ADMIN"; // 此次登录的客户端设备类型, 用于[同端互斥登录]时指定此次登录的设备类型
		String tokenValue = tokenService.generate(String.valueOf(sysUserId), device);
		if (StringUtils.isNoneBlank(tokenPrefix)) {
			tokenValue = tokenPrefix + " " + tokenValue;
		}
		
		publishLoginEvent(sysUserId, device, account);

		LoginResp resp = new LoginResp();
		resp.setToken(tokenValue);
		resp.setTips(sysUserPasswordResp.getPasswordTips());
		return Result.success(resp);
	}

	// 发布登录事件
	private void publishLoginEvent(Integer sysUserId, String device, String account) {
		Map<String, Object> params = Maps.newHashMap();
		params.put("device", device);
		params.put("sysUserId", sysUserId);
		params.put("account", account);
		params.put("loginTime", LocalDateTimeUtil.formatNormal(LocalDateTime.now()));
		params.put("httpContextHeader", HttpContextUtil.httpContextHeader());
		messageSender.sendFanoutMessage(params, FanoutConstants.SYS_USER_LOGIN.EXCHANGE);
	}
	
	@RequireLogin
	@PostMapping(value = "/logout")
	public Result<String> logout(HttpServletRequest request) {
		String token = request.getHeader(headerToken);
		token = TokenValueUtil.fixToken(tokenPrefix, token);
		if (StringUtils.isBlank(token)) {
			return Result.success("登出成功");
		}

		tokenService.invalid(token);

		return Result.success("登出成功");
	}
}
