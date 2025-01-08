package com.company.web.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.company.common.api.Result;
import com.company.common.util.RegexUtil;
import com.company.framework.amqp.MessageSender;
import com.company.framework.amqp.constants.FanoutConstants;
import com.company.framework.annotation.RequireLogin;
import com.company.framework.context.HttpContextUtil;
import com.company.tool.api.feign.VerifyCodeFeign;
import com.company.user.api.enums.UserOauthEnum;
import com.company.user.api.feign.UserInfoFeign;
import com.company.user.api.feign.UserOauthFeign;
import com.company.user.api.request.UserInfoReq;
import com.company.user.api.response.UserInfoResp;
import com.company.user.api.response.UserOauthResp;
import com.company.web.constants.Constants;
import com.company.web.req.LoginByEmailReq;
import com.company.web.req.LoginByMobileReq;
import com.company.web.req.RegByEmailReq;
import com.company.web.req.RegByMobileReq;
import com.company.web.resp.LoginResp;
import com.company.web.token.TokenService;
import com.company.web.util.PassWordUtil;
import com.company.web.util.TokenValueUtil;
import com.google.common.collect.Maps;
import com.jqdi.easylogin.core.LoginClient;
import com.jqdi.easylogin.core.exception.LoginException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Validated
@RestController
@RequestMapping("/account")
public class AccountController {
	
	@Autowired
	private UserInfoFeign userInfoFeign;
	@Autowired
	private UserOauthFeign userOauthFeign;

	@Autowired
	private TokenService tokenService;
	@Autowired
	private MessageSender messageSender;
	@Autowired
	private VerifyCodeFeign verifyCodeFeign;
	
	@Autowired
	@Qualifier("emailPasswordCode")
	private LoginClient emailPasswordCodeLoginClient;
	@Autowired
	@Qualifier("mobilePasswordCode")
	private LoginClient mobilePasswordCodeLoginClient;

	@Value("${token.name}")
	private String headerToken;
	
	@Value("${token.prefix:}")
	private String tokenPrefix;

	@GetMapping(value = "/reg/verify/email")
	public Result<String> regVerifyByEmail(@NotBlank(message = "邮箱不能为空") String email) {
		if (!RegexUtil.checkEmail(email)) {
			return Result.fail("邮箱格式错误");
		}

		String identifier = email;
		UserOauthResp userOauthResp = userOauthFeign.selectOauth(UserOauthEnum.IdentityType.EMAIL, identifier).dataOrThrow();
		if (userOauthResp != null) {
			return Result.fail("邮箱已注册，可以直接登录！");
		}

		return verifyCodeFeign.email(email, Constants.VerifyCodeType.REGISTER);
	}

	@PostMapping(value = "/reg/email")
	public Result<String> regByEmail(@Valid @RequestBody RegByEmailReq regByEmailReq) {
		String email = regByEmailReq.getEmail();
		if (!RegexUtil.checkEmail(email)) {
			return Result.fail("邮箱格式错误");
		}
		
		String identifier = email;
		UserOauthResp userOauthResp = userOauthFeign.selectOauth(UserOauthEnum.IdentityType.EMAIL, identifier).dataOrThrow();
		if (userOauthResp != null) {
			return Result.fail("邮箱已注册，可以直接登录！");
		}

		Boolean verifyPass = verifyCodeFeign.verify(Constants.VerifyCodeType.REGISTER, email, regByEmailReq.getCode()).dataOrThrow();
		if (!verifyPass) {
			return Result.fail("验证码错误");
		}

		// TODO 密码复杂度校验

		UserInfoReq userInfoReq = new UserInfoReq();
		userInfoReq.setIdentityType(UserOauthEnum.IdentityType.EMAIL);
		userInfoReq.setIdentifier(email);
		userInfoReq.setCertificate(PassWordUtil.md5(regByEmailReq.getPassword()));
//		userInfoReq.setAvatar(null);
//		userInfoReq.setNickname(null);
		UserInfoResp userInfoResp = userInfoFeign.findOrCreateUser(userInfoReq).dataOrThrow();
		log.info("userId:{}", userInfoResp.getId());

		return Result.success("注册成功");
	}

	@GetMapping(value = "/reg/verify/mobile")
	public Result<String> regVerifyByMobile(@NotBlank(message = "手机号不能为空") String mobile) {
		if (!RegexUtil.checkMobile(mobile)) {
			return Result.fail("手机号格式错误");
		}

		String identifier = mobile;
		UserOauthResp userOauthResp = userOauthFeign.selectOauth(UserOauthEnum.IdentityType.MOBILE, identifier)
				.dataOrThrow();
		if (userOauthResp != null) {
			return Result.fail("手机号已注册，可以直接登录！");
		}
		
		return verifyCodeFeign.sms(mobile, Constants.VerifyCodeType.REGISTER);
	}

	@PostMapping(value = "/reg/mobile")
	public Result<String> regByMobile(@Valid @RequestBody RegByMobileReq regByMobileReq) {
		String mobile = regByMobileReq.getMobile();
		if (!RegexUtil.checkMobile(mobile)) {
			return Result.fail("手机号格式错误");
		}
		
		String identifier = mobile;
		UserOauthResp userOauthResp = userOauthFeign.selectOauth(UserOauthEnum.IdentityType.MOBILE, identifier)
				.dataOrThrow();
		if (userOauthResp != null) {
			return Result.fail("手机号已注册，可以直接登录！");
		}

		Boolean verifyPass = verifyCodeFeign.verify(Constants.VerifyCodeType.REGISTER, mobile, regByMobileReq.getCode()).dataOrThrow();
		if (!verifyPass) {
			return Result.fail("验证码错误");
		}

		// TODO 密码复杂度校验
		
		UserInfoReq userInfoReq = new UserInfoReq();
		userInfoReq.setIdentityType(UserOauthEnum.IdentityType.MOBILE);
		userInfoReq.setIdentifier(identifier);
		userInfoReq.setCertificate(PassWordUtil.md5(regByMobileReq.getPassword()));
//		userInfoReq.setAvatar(null);
//		userInfoReq.setNickname(null);
		UserInfoResp userInfoResp = userInfoFeign.findOrCreateUser(userInfoReq).dataOrThrow();
		log.info("userId:{}", userInfoResp.getId());

		return Result.success("注册成功");
	}

	@GetMapping(value = "/login/verify/email")
	public Result<String> loginVerifyByEmail(@NotBlank(message = "邮箱不能为空") String email) {
		if (!RegexUtil.checkEmail(email)) {
			return Result.fail("邮箱格式错误");
		}

		String identifier = email;
		UserOauthResp userOauthResp = userOauthFeign.selectOauth(UserOauthEnum.IdentityType.EMAIL, identifier).dataOrThrow();
		if (userOauthResp == null) {
			return Result.fail("邮箱未注册，请前往注册！");
		}

		return verifyCodeFeign.email(email, Constants.VerifyCodeType.LOGIN);
	}

	@PostMapping(value = "/login/email")
	public Result<LoginResp> loginByEmail(@Valid @RequestBody LoginByEmailReq loginByEmailReq) {
		String email = loginByEmailReq.getEmail();
		String password = loginByEmailReq.getPassword();
		String code = loginByEmailReq.getCode();

		String identifier = email;
		String userId = null;
		try {
			userId = emailPasswordCodeLoginClient.login(identifier, password, code);
		} catch (LoginException e) {
			return Result.fail(e.getMessage());
		}
		
		LoginResp loginResp = new LoginResp();
		loginResp.setNeedBind(false);
		loginResp.setToken(token(userId));

		return Result.success(loginResp);
	}
	
	@GetMapping(value = "/login/verify/mobile")
	public Result<String> loginVerifyByMobile(@NotBlank(message = "手机号不能为空") String mobile) {
		if (!RegexUtil.checkMobile(mobile)) {
			return Result.fail("手机号格式错误");
		}
		
		String identifier = mobile;
		UserOauthResp userOauthResp = userOauthFeign.selectOauth(UserOauthEnum.IdentityType.MOBILE, identifier)
				.dataOrThrow();
		if (userOauthResp == null) {
			return Result.fail("手机号未注册，请前往注册！");
		}
		
		return verifyCodeFeign.sms(mobile, Constants.VerifyCodeType.LOGIN);
	}
	
	@PostMapping(value = "/login/mobile")
	public Result<LoginResp> loginByMobile(@Valid @RequestBody LoginByMobileReq loginByMobileReq) {
		String mobile = loginByMobileReq.getMobile();
		String password = loginByMobileReq.getPassword();
		String code = loginByMobileReq.getCode();
		
		String identifier = mobile;
		String userId = null;
		try {
			userId = mobilePasswordCodeLoginClient.login(identifier, password, code);
		} catch (LoginException e) {
			return Result.fail(e.getMessage());
		}
		
		LoginResp loginResp = new LoginResp();
		loginResp.setNeedBind(false);
		loginResp.setToken(token(userId));

		return Result.success(loginResp);
	}

	private String token(String userId) {
		String device = "WEB"; // 此次登录的客户端设备类型, 用于[同端互斥登录]时指定此次登录的设备类型

		String tokenValue = tokenService.generate(userId, device);
		if (StringUtils.isNoneBlank(tokenPrefix)) {
			tokenValue = tokenPrefix + " " + tokenValue;
		}

		// 发布登录事件
		Map<String, Object> params = Maps.newHashMap();
		params.put("device", device);
		params.put("userId", userId);
		params.put("httpContextHeader", HttpContextUtil.httpContextHeader());
		messageSender.sendFanoutMessage(params, FanoutConstants.USER_LOGIN.EXCHANGE);

		return tokenValue;
	}

	@RequireLogin
	@PostMapping(value = "/logout")
	public Result<String> logout(HttpServletRequest request) {
		String token = request.getHeader(headerToken);
		token = TokenValueUtil.fixToken(tokenPrefix, token);
		if (StringUtils.isBlank(token)) {
			return Result.success("登出成功");
		}

		String device = tokenService.invalid(token);

		// 发布登出事件
		Map<String, Object> params = Maps.newHashMap();
		params.put("device", device);
		params.put("userId", HttpContextUtil.currentUserId());
		params.put("httpContextHeader", HttpContextUtil.httpContextHeader());
		messageSender.sendFanoutMessage(params, FanoutConstants.USER_LOGOUT.EXCHANGE);

		return Result.success("登出成功");
	}
}
