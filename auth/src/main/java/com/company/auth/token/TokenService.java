package com.company.auth.token;

public interface TokenService {
	/**
	 * 生成
	 * 
	 * @return token
	 */
	String generate(String userId, String device);

	/**
	 * 检查token
	 * 
	 * @return
	 */
//	String checkAndGet(String token);// 检查token在zuul网关
}