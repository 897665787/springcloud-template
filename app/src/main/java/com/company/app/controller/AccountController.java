package com.company.app.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import com.company.framework.context.UserContextUtil;
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

import com.company.app.constants.Constants;
import com.company.app.req.LoginByMobileReq;
import com.company.app.req.LoginByWeixinAppReq;
import com.company.app.resp.LoginResp;
import com.company.app.token.TokenService;
import com.company.app.util.TokenValueUtil;
import com.company.common.api.Result;
import com.company.framework.util.RegexUtil;
import com.company.framework.messagedriven.MessageSender;
import com.company.framework.messagedriven.constants.FanoutConstants;
import com.company.framework.annotation.RequireLogin;
import com.company.framework.context.HttpContextUtil;
import com.company.tool.api.feign.VerifyCodeFeign;
import com.company.user.api.enums.UserOauthEnum;
import com.company.user.api.feign.UserOauthFeign;
import com.company.user.api.response.UserOauthResp;
import com.google.common.collect.Maps;
import com.jqdi.easylogin.core.LoginClient;
import com.jqdi.easylogin.core.exception.LoginException;
import com.jqdi.easylogin.spring.boot.starter.LoginType;

@Validated
@RestController
@RequestMapping("/account")
public class AccountController {

	@Autowired
	private UserOauthFeign userOauthFeign;

	@Autowired
	private TokenService tokenService;
	@Autowired
	private MessageSender messageSender;
	@Autowired
	private VerifyCodeFeign verifyCodeFeign;

	@Autowired
	@Qualifier(LoginType.WEIXIN_APP)
	private LoginClient weixinAppLoginClient;

	@Autowired
	@Qualifier(LoginType.MOBILE_CODE_BIND)
	private LoginClient mobileCodeBindLoginClient;

	@Value("${token.name}")
	private String headerToken;

	@Value("${token.prefix:}")
	private String tokenPrefix;

	@PostMapping(value = "/login/weixinapp")
	public Result<LoginResp> loginByMobile(@Valid @RequestBody LoginByWeixinAppReq loginByWeixinAppReq) {
		String wxcode = loginByWeixinAppReq.getWxcode();

		String userId = null;
		try {
			userId = weixinAppLoginClient.login(null, null, wxcode);
		} catch (LoginException e) {
			return Result.fail(e.getMessage());
		}

		LoginResp loginResp = new LoginResp();
		if (userId == null) {
			// 用户ID为null代表未登录成功，一般是指通过authcode没有找到对应的用户，需要通过XxxCodeBind来注册用户账号
			loginResp.setNeedBind(true);
			loginResp.setBindCode(wxcode);
			return Result.success(loginResp);
		}

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
		String code = loginByMobileReq.getCode();
		String bindCode = loginByMobileReq.getBindCode();

		String identifier = mobile;
		String userId = null;
		try {
			userId = mobileCodeBindLoginClient.login(identifier, code, bindCode);
		} catch (LoginException e) {
			return Result.fail(e.getMessage());
		}

		LoginResp loginResp = new LoginResp();
		loginResp.setNeedBind(false);
		loginResp.setToken(token(userId));

		return Result.success(loginResp);
	}

	private String token(String userId) {
		String device = "APP"; // 此次登录的客户端设备类型, 用于[同端互斥登录]时指定此次登录的设备类型

		String tokenValue = tokenService.generate(userId, device);
		if (StringUtils.isNoneBlank(tokenPrefix)) {
			tokenValue = tokenPrefix + " " + tokenValue;
		}

		// 发布登录事件
		Map<String, Object> params = Maps.newHashMap();
		params.put("device", device);
		params.put("userId", userId);
		params.put("httpContextHeader", HttpContextUtil.httpContextHeader());
		params.put("time", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
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
		params.put("userId", UserContextUtil.currentUserId());
		params.put("httpContextHeader", HttpContextUtil.httpContextHeader());
		params.put("time", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
		messageSender.sendFanoutMessage(params, FanoutConstants.USER_LOGOUT.EXCHANGE);

		return Result.success("登出成功");
	}
}
