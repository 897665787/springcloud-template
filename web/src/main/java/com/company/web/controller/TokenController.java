package com.company.web.controller;

import java.util.Date;
import java.util.Map;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.company.common.annotation.PublicUrl;
import com.company.framework.context.HttpContextUtil;
import com.company.web.filter.TokenUtil;

@RestController
@RequestMapping("/token")
public class TokenController {
	
	public static void main(String[] args) {
		String subject = "subject";
		String audience = "APP";// APP MINIP
		Date expiration = DateUtils.addSeconds(new Date(), 5);
//		SpringContextUtil.getProperty("spring.application.name");
		String token = TokenUtil.generateToken(subject, audience, expiration);
//		String token = "eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJTRVJWSUNFIiwic3ViIjoic3ViamVjdCIsImF1ZCI6IkFQUCIsImV4cCI6MTYzOTIzMDcxMywibmJmIjoxNjM5MjMwNzA4LCJpYXQiOjE2MzkyMzA3MDgsImp0aSI6ImlkIn0.eMNz_Tm8i1xwKkO3NVkw5omLAejAXywQ89WYxddhC98";
		String userId = TokenUtil.checkToken(token);
		System.out.println("userId:" + userId);
	}
	
	@PostMapping(value = "/login")
	@PublicUrl
	public String login(@RequestBody Map<String, Object> param) {
		// 手机号+验证码
		// 手机号+密码
		// 小程序微信授权(根据code找到openid或unionid)
//		WxMaJscode2SessionResult session = wxService.getUserService().getSessionInfo(code);
		// APP微信授权(根据code找到openid或unionid)
		
		String userId = "1";
		
		String subject = userId;
		String audience = HttpContextUtil.platform();
//		Date expiration = DateUtils.addSeconds(new Date(), 5);
		Date expiration = DateUtils.addMinutes(new Date(), 50);
//		SpringContextUtil.getProperty("spring.application.name");
		String token = TokenUtil.generateToken(subject, audience, expiration);
		
		return token;
	}
	
	@PostMapping(value = "/refresh")
	public String refresh(@RequestBody Map<String, Object> param) {
		String token = null;
		String userId = TokenUtil.checkToken(token);
		if(userId != null){
			String subject = userId;
			String audience = HttpContextUtil.platform();
			Date expiration = DateUtils.addSeconds(new Date(), 5);
			token = TokenUtil.generateToken(subject, audience, expiration);
		}
		return token;
	}
}
