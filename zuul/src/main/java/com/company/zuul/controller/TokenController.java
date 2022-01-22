package com.company.zuul.controller;

import java.util.Date;
import java.util.Map;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.company.common.annotation.PublicUrl;
import com.company.framework.context.HttpContextUtil;
import com.company.zuul.filter.TokenUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/token")
public class TokenController {
	
	@PostMapping(value = "/login")
	@PublicUrl
	public String login(@RequestBody Map<String, Object> param) {
		// 手机号+验证码
		// 手机号+密码
		// 小程序微信授权(根据code找到openid或unionid)
//		WxMaJscode2SessionResult session = wxService.getUserService().getSessionInfo(code);
		// APP微信授权(根据code找到openid或unionid)
		
		String userId = "83848";
		
		String subject = userId;
		String audience = HttpContextUtil.platform();
		Date expiration = DateUtils.addSeconds(new Date(), 5);
//		Date expiration = DateUtils.addMinutes(new Date(), 50);
		String token = TokenUtil.generateToken(subject, audience, expiration);
		log.info("subject:{} token:{}", subject, token);
		return token;
	}
	
	@PostMapping(value = "/refresh")
	public String refresh(@RequestBody Map<String, Object> param) {
		String token = null;
		String userId = TokenUtil.checkTokenAndGetSubject(token, true);
		if(userId != null){
			String subject = userId;
			String audience = HttpContextUtil.platform();
			Date expiration = DateUtils.addSeconds(new Date(), 5);
			token = TokenUtil.generateToken(subject, audience, expiration);
		}
		return token;
	}
}
