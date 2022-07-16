package com.company.zuul.token.satoken;

import java.util.Date;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import com.company.zuul.token.TokenService;

import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;

@Primary
@Component
public class SaTokenService implements TokenService {

	public String generate(String subject, String audience, Date expiration) {
		StpUtil.login(subject);
		SaTokenInfo tokenInfo = StpUtil.getTokenInfo();
		System.out.println("getTokenName():" + tokenInfo.getTokenName());
		System.out.println("getTokenValue():" + tokenInfo.getTokenValue());
		return tokenInfo.getTokenValue();
	}

	public String checkAndGet(String token) {
		return StpUtil.getLoginIdAsString();
	}
}