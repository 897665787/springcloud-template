package com.company.zuul.token;

import java.util.Date;

public interface TokenService {
	/**
	 * 生成
	 * 
	 * @return token
	 */
	String generate(String subject, String audience, Date expiration);

	/**
	 * 检查token
	 * 
	 * @return
	 */
	String checkAndGet(String token);
}