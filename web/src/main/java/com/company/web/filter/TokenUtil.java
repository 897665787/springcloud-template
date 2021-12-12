package com.company.web.filter;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import lombok.extern.slf4j.Slf4j;

/**
 * token解析
 */
@Slf4j
public class TokenUtil {
	private static final String secret = "11111111";
	
	private TokenUtil() {
	}

	public static String generateToken(String subject, String audience, Date expiration) {
		String issuer = "SERVICE";
//		String subject = "subject";
//		String audience = "APP";// APP MINIP
//		Date expiration = DateUtils.addSeconds(new Date(), 5);
		Date not_before = new Date();
		Date issued_at = new Date();
		String jti = RandomUtil.simpleUUID();
		
		/**
		 * <pre>
		Claims claims = Jwts.claims();
		claims.put(Claims.ISSUER, issuer);
		claims.put(Claims.SUBJECT, subject);
		claims.put(Claims.AUDIENCE, audience);
		claims.put(Claims.EXPIRATION, expiration.getTime() / 1000);
		claims.put(Claims.NOT_BEFORE, not_before.getTime() / 1000);
		claims.put(Claims.ISSUED_AT, issued_at.getTime() / 1000);
		claims.put(Claims.ID, jti);
		
		// 自定义claims，一般不需要用到
		claims.put("cccc", "APP");
		 * </pre>
		 */

		String token = Jwts.builder()
				// 添加自定义头部，一般不需要用到
				// .setHeaderParam("asdasd", "ddddd")

				// claims 等价于setXxx
				// .setClaims(claims)

				// 等价于claims
				.setIssuer(issuer)
				.setSubject(subject)
				.setAudience(audience)
				.setExpiration(expiration)
				.setNotBefore(not_before)
				.setIssuedAt(issued_at)
				.setId(jti)

				// 签名
				.signWith(SignatureAlgorithm.HS256, secret)
				.compact();

		log.info("token:{}", token);
		return token;
	}
	
	public static String checkToken(String token) {
		if (StringUtils.isBlank(token)) {
			return null;
		}
		
		Jws<Claims> parseClaimsJws = null;
		try {
			parseClaimsJws = Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
		} catch (SignatureException e) {
			// token不正确
			log.error("SignatureException", e);
			return null;
		} catch (ExpiredJwtException e) {
			// token过期
			System.out.println(JSONUtil.toJsonPrettyStr(e.getClaims()));
			// System.out.println(JSONUtil.toJsonPrettyStr(e.getHeader()));
			log.error("ExpiredJwtException", e);
			return null;
		}

//		System.out.println(JSONUtil.toJsonPrettyStr(parseClaimsJws));

		Claims body = parseClaimsJws.getBody();

//		System.out.println(JSONUtil.toJsonPrettyStr(body));
		Date expiration = body.getExpiration();
		if (expiration.before(new Date())) {
			// token过期
			return null;
		}
		String subject = body.getSubject();
//		String id = body.getId();
//		String string = RedisUtils.get("jwt:jti:" + id);
//		userId 登录平台 tokenid
		
		// token校验通过，取出重要信息进行传递或校验
		
		/* =============== 同样的登录平台，旧的登录token失效=============== */
		/*
		String audience = body.getAudience();
		String tokenid = RedisUtils.get("jwt:aud:" + audience + ":sub:" + subject);
		if (!id.equals(tokenid)) {
			// 同样的登录平台，旧的登录token失效
			return null;
		}*/
		/* =============== 同样的登录平台，旧的登录token失效=============== */

		/* =============== 系统让token失效=============== */
		/*String tokenid2 = RedisUtils.get("jwt:jti:" + id);
		if (tokenid2 == null) {
			// 找不到tokenid，说明被失效了
			return null;
		}*/
		/* =============== 系统让token失效=============== */
		
//		另外1台设备登录
//		userId APP
//		userId MINIP
		
		return subject;
	}
}