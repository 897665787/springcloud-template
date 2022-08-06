package com.company.zuul.token;

public interface TokenService {
	/**
	 * 生成
	 * 
	 * @return token
	 */
//	String generate(String subject, String audience);// 生成token在auth认证中心

	/**
	 * 检查token
	 * 
	 * @return
	 */
	String checkAndGet(String token);
}
