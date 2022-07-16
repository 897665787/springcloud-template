package com.company.auth.token.jsonwebtoken;

import java.util.Date;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.company.auth.token.TokenService;
import com.company.auth.token.jsonwebtoken.util.TokenUtil;

@Component
public class JsonWebTokenService implements TokenService {
	
	@Value("${template.enable.access-control:true}")
	private Boolean enableAccessControl;
	
	@Value("${token.timeout:2592000}")
	private Integer timeout;
		  
	public String generate(String userId, String device) {
		Date expiration = DateUtils.addSeconds(new Date(), timeout);
		return TokenUtil.generateToken(userId, device, expiration);
	}

	public String checkAndGet(String token) {
		return TokenUtil.checkTokenAndGetSubject(token, enableAccessControl);
	}
}