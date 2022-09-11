package com.company.auth.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.company.auth.authentication.LoginBeanFactory;
import com.company.auth.authentication.LoginService;
import com.company.auth.authentication.LoginType;
import com.company.auth.authentication.dto.LoginResult;
import com.company.auth.resp.LoginResp;
import com.company.auth.token.TokenService;
import com.company.common.annotation.PublicUrl;
import com.company.common.api.Result;
import com.company.common.util.JsonUtil;
import com.company.framework.amqp.MessageSender;
import com.company.framework.amqp.rabbit.constants.FanoutConstants;
import com.google.common.collect.Maps;

import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/account")
public class AccountController {
	
	@Autowired
	private TokenService tokenService;
	@Autowired
	private MessageSender messageSender;
	
	@PublicUrl
	@GetMapping(value = "/login")
	public Result<LoginResp> login(String loginType, String mobile, String code, String authcode, String device) {
		LoginType.Enum loginTypeEnum = LoginType.Enum.of(loginType);
		if (loginTypeEnum == null) {
			return Result.fail("登录类型不存在");
		}
		LoginService loginService = LoginBeanFactory.of(loginTypeEnum);
		LoginResult loginResult = loginService.login(mobile, code, authcode);
		if (!loginResult.getSuccess()) {
			return Result.fail(loginResult.getMessage());
		}
		Integer userId = loginResult.getUserId();
		String tokenValue = tokenService.generate(String.valueOf(userId), device);
		
		LoginResp loginResp = new LoginResp();
		loginResp.setUserId(userId);
		loginResp.setToken(tokenValue);
		
		// 发布登录事件
		Map<String, Object> params = Maps.newHashMap();
		params.put("loginType", loginType);
		params.put("device", device);
		params.put("userId", userId);
		params.put("tokenValue", tokenValue);
		messageSender.sendFanoutMessage(params, FanoutConstants.USER_LOGIN.EXCHANGE);
		
		return Result.success(loginResp);
	}
	
	@GetMapping(value = "/logout")
	public Result<String> logout() {
		StpUtil.logout();
		SaTokenInfo tokenInfo = StpUtil.getTokenInfo();
		log.info("tokenInfo:{}", JsonUtil.toJsonString(tokenInfo));
		// 发布登出事件
		return Result.success("登出成功");
	}
}
