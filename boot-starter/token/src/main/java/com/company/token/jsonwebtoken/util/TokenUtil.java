package com.company.token.jsonwebtoken.util;

import cn.hutool.core.util.RandomUtil;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.util.Date;

/**
 * token工具类
 */
@Slf4j
public class TokenUtil {

	private TokenUtil() {
	}

	public static String generateToken(String subject, String audience, Date expiration, String secret) {
		String issuer = "SERVICE";
		Date not_before = new Date();
		Date issued_at = new Date();
		String jti = RandomUtil.randomString(32);

		/**
		 * <pre>
		 * Claims claims = Jwts.claims();
		 * claims.put(Claims.ISSUER, issuer);
		 * claims.put(Claims.SUBJECT, subject);
		 * claims.put(Claims.AUDIENCE, audience);
		 * claims.put(Claims.EXPIRATION, expiration.getTime() / 1000);
		 * claims.put(Claims.NOT_BEFORE, not_before.getTime() / 1000);
		 * claims.put(Claims.ISSUED_AT, issued_at.getTime() / 1000);
		 * claims.put(Claims.ID, jti);
		 * 
		 * // 自定义claims，一般不需要用到
		 * claims.put("cccc", "APP");
		 * </pre>
		 */

		String token = Jwts.builder()
				// 添加自定义头部，一般不需要用到
				// .setHeaderParam("myheader", "myheaderval")

				// claims 等价于setXxx
				// .setClaims(claims)

                // 等价于claims
                .issuer(issuer).subject(subject).audience().add(audience).and().expiration(expiration).notBefore(not_before)
                .issuedAt(issued_at).id(jti)

				// 签名
                .signWith(Keys.hmacShaKeyFor(secret.getBytes()))
                .compact();
		return token;
	}

	public static String checkTokenAndGetSubject(String token, boolean returnNullIfExpired, String secret) {
		if (StringUtils.isBlank(token)) {
			return null;
		}

		Claims body = null;
		boolean expired = false;
		try {
            Jws<Claims> claimsJws = Jwts.parser().verifyWith(Keys.hmacShaKeyFor(secret.getBytes())).build().parseSignedClaims(token);
			body = claimsJws.getPayload();
		} catch (ExpiredJwtException e) {// token过期
			// log.error("ExpiredJwtException", e);
			body = e.getClaims();
			expired = true;
		}
		if (expired && returnNullIfExpired) {
			// token过期
			return null;
		}
		String subject = body.getSubject();
		// String id = body.getId();
		// String string = RedisUtils.get("jwt:jti:" + id);
		// userId 登录平台 tokenid

		// token校验通过，取出重要信息进行传递或校验

		/* =============== 同样的登录平台，旧的登录token失效=============== */
		/*
		 * String audience = body.getAudience(); String tokenid =
		 * RedisUtils.get("jwt:aud:" + audience + ":sub:" + subject); if
		 * (!id.equals(tokenid)) { // 同样的登录平台，旧的登录token失效 return null; }
		 */
		/* =============== 同样的登录平台，旧的登录token失效=============== */

		/* =============== 系统让token失效=============== */
		/*
		 * String tokenid2 = RedisUtils.get("jwt:jti:" + id); if (tokenid2 ==
		 * null) { // 找不到tokenid，说明被失效了 return null; }
		 */
		/* =============== 系统让token失效=============== */
		return subject;
	}
	
	public static Claims getClaims(String token, String secret) {
		if (StringUtils.isBlank(token)) {
			return null;
		}

		Claims body = null;
		try {
            Jws<Claims> claimsJws = Jwts.parser().verifyWith(Keys.hmacShaKeyFor(secret.getBytes())).build().parseSignedClaims(token);
            body = claimsJws.getPayload();
		} catch (ExpiredJwtException e) {// token过期
			// log.error("ExpiredJwtException", e);
			body = e.getClaims();
		}
		return body;
	}

    public static void main(String[] args) {
        String secret = "52ae521312f6461083435e045900486e";// 密钥，至少32字节
        String subject = "83848";
        String audience = "APP";// APP MINIP
        Date expiration = DateUtils.addSeconds(new Date(), 5);
        String token = TokenUtil.generateToken(subject, audience, expiration, secret);
//		String token = "eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJTRVJWSUNFIiwic3ViIjoiODM4NDgiLCJhdWQiOiJBUFAiLCJleHAiOjE2Mzk5MjU1ODIsIm5iZiI6MTYzOTkyNTU3NywiaWF0IjoxNjM5OTI1NTc3LCJqdGkiOiI5NzcyNTU4MGJjYjM0N2E1ODljZTZiYjkxZjY4OWZhZiJ9.-W6Z-VOffBLwvIipDT_LwUVhbLhJAFff_arh8Iu93go";
        System.out.println("token:" + token);
        String userId = TokenUtil.checkTokenAndGetSubject(token, false, secret);
        System.out.println("userId:" + userId);
        log.info("token:" + token);
    }
}