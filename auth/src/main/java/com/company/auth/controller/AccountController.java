package com.company.auth.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.company.auth.authentication.LoginBeanFactory;
import com.company.auth.authentication.LoginService;
import com.company.auth.authentication.LoginType;
import com.company.auth.authentication.dto.LoginResult;
import com.company.auth.resp.LoginResp;
import com.company.auth.token.TokenService;
import com.company.common.api.Result;
import com.company.framework.amqp.MessageSender;
import com.company.framework.amqp.rabbit.constants.FanoutConstants;
import com.company.framework.annotation.RequireLogin;
import com.company.framework.context.HttpContextUtil;
import com.google.common.collect.Maps;

@RestController
@RequestMapping("/account")
public class AccountController {
	
	@Autowired
	private TokenService tokenService;
	@Autowired
	private MessageSender messageSender;
	
	@Value("${token.name}")
	private String headerToken;
	
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
		if (userId == null) {
			// 用户ID为null代表未登录成功，一般是指通过authcode没有找到对应的用户，需要通过mobileCodeBind来注册用户账号
			LoginResp loginResp = new LoginResp();
			loginResp.setNeedBind(true);
			return Result.success(loginResp);
		}
		
		String tokenValue = tokenService.generate(String.valueOf(userId), device);
		
		LoginResp loginResp = new LoginResp();
		loginResp.setNeedBind(false);
//		loginResp.setUserId(userId);
		loginResp.setToken(tokenValue);
		
		// 发布登录事件
		Map<String, Object> params = Maps.newHashMap();
		params.put("loginType", loginType);
		params.put("device", device);
		params.put("userId", userId);
//		params.put("tokenValue", tokenValue);
		params.put("httpContextHeader", HttpContextUtil.httpContextHeader());
		messageSender.sendFanoutMessage(params, FanoutConstants.USER_LOGIN.EXCHANGE);
		
		return Result.success(loginResp);
	}
	
	@RequireLogin
	@GetMapping(value = "/logout")
	public Result<String> logout(HttpServletRequest request) {
		String token = request.getHeader(headerToken);
		String device = tokenService.invalid(token);
		
		// 发布登出事件
		Map<String, Object> params = Maps.newHashMap();
		params.put("device", device);
		params.put("userId", HttpContextUtil.currentUserId());
//		params.put("tokenValue", tokenValue);
		params.put("httpContextHeader", HttpContextUtil.httpContextHeader());
		messageSender.sendFanoutMessage(params, FanoutConstants.USER_LOGOUT.EXCHANGE);

		return Result.success("登出成功");
	}
}
