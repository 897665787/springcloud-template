package com.company.token.jsonwebtoken;

import com.company.token.TokenService;
import com.company.token.jsonwebtoken.util.TokenUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Value;

import java.util.Date;

@Slf4j
public class JsonWebTokenService implements TokenService {

    @Value("${token.timeout:2592000}")
    private Integer timeout;

    @Value("${token.secret:defaultsecret}")
    private String secret;

    @Value("${template.enable.access-control:true}")
    private Boolean enableAccessControl;

    @Override
    public String generate(String userId, String device) {
        Date expiration = DateUtils.addSeconds(new Date(), timeout);
        return TokenUtil.generateToken(userId, device, expiration, secret);
    }

    @Override
    public String invalid(String token) {
        Claims claims = TokenUtil.getClaims(token, secret);
        log.info("claims:{}", claims);

        // do nothing
        if (claims == null) {
            return null;
        }
        return claims.getAudience().iterator().next();
    }

    @Override
    public String checkAndGet(String token) {
        return TokenUtil.checkTokenAndGetSubject(token, enableAccessControl, secret);
    }

}
