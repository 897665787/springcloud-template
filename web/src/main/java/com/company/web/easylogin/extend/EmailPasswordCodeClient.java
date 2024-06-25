package com.company.web.easylogin.extend;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.jqdi.easylogin.core.LoginClient;
import com.jqdi.easylogin.core.constants.IdentityType;
import com.jqdi.easylogin.core.exception.LoginException;
import com.jqdi.easylogin.core.repository.OauthRepository;
import com.jqdi.easylogin.core.repository.PasswordRepository;
import com.jqdi.easylogin.core.repository.VerifycodeRepository;

@Component("emailPasswordCode")
public class EmailPasswordCodeClient implements LoginClient {
	private OauthRepository oauthRepository;
	private PasswordRepository passwordRepository;
	private VerifycodeRepository verifycodeRepository;

	public EmailPasswordCodeClient(OauthRepository oauthRepository, PasswordRepository passwordRepository,
			VerifycodeRepository verifycodeRepository) {
		this.oauthRepository = oauthRepository;
		this.passwordRepository = passwordRepository;
		this.verifycodeRepository = verifycodeRepository;
	}

	@Override
	public String login(String email, String password, String code) {
		if (StringUtils.isBlank(email) || StringUtils.isBlank(password) || StringUtils.isBlank(code)) {
			throw new LoginException("缺失参数");
		}
		// 核对验证码
		boolean checkVerifyCode = verifycodeRepository.checkVerifycode(email, code);
		if (!checkVerifyCode) {
			throw new LoginException("验证码错误");
		}

		// 核对密码
		String userId = oauthRepository.getUserId(IdentityType.EMAIL, email);
		boolean checkPassword = passwordRepository.checkPassword(userId, password);
		if (!checkPassword) {
			throw new LoginException("密码错误");
		}
		
		return userId;
	}
}
