package com.company.web.token;

public interface TokenService {
	/**
	 * 生成
	 * 
	 * @return token
	 */
	String generate(String userId, String device);

	/**
	 * 失效token
	 * 
	 * @return 登录设备
	 */
	String invalid(String token);

	/**
	 * 检查token
	 * 
	 * @return
	 */
	String checkAndGet(String token);
}