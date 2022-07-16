package com.company.zuul.token.jsonwebtoken;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.company.zuul.token.TokenService;
import com.company.zuul.token.jsonwebtoken.util.TokenUtil;

@Component
public class JsonWebTokenService implements TokenService {
	
	@Value("${template.enable.access-control:true}")
	private Boolean enableAccessControl;
	
	public String generate(String subject, String audience, Date expiration) {
		return TokenUtil.generateToken(subject, audience, expiration);
	}

	public String checkAndGet(String token) {
		return TokenUtil.checkTokenAndGetSubject(token, enableAccessControl);
	}
}