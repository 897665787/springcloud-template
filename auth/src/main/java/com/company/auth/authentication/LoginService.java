package com.company.auth.authentication;

import com.company.auth.authentication.dto.LoginResult;

public interface LoginService {
	/**
	 * 登录
	 * 
	 * @param mobileOrUsernameOrEncryptedData
	 *            用户名|手机号|加密数据
	 * @param codeOrPasswordOrIv
	 *            验证码|密码|解密偏移量
	 * @param authcode
	 *            授权code
	 * @return
	 */
	LoginResult login(String mobileOrUsernameOrEncryptedData, String codeOrPasswordOrIv, String authcode);
}
