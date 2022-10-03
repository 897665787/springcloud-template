package com.company.gateway.token.jsonwebtoken;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.company.gateway.token.TokenService;
import com.company.gateway.token.jsonwebtoken.util.TokenUtil;

@Component // 优先级低于SaTokenService，作为token备用方案
public class JsonWebTokenService implements TokenService {
	
	@Value("${template.enable.access-control:true}")
	private Boolean enableAccessControl;
	
	@Value("${token.secret:defaultsecret}")
	private String secret;
	
	/*
	@Override
	public String generate(String userId, String device) {
		Date expiration = DateUtils.addSeconds(new Date(), timeout);
		return TokenUtil.generateToken(userId, device, expiration, secret);
	}
	*/
	
	@Override
	public String checkAndGet(String token) {
		return TokenUtil.checkTokenAndGetSubject(token, enableAccessControl, secret);
	}
}