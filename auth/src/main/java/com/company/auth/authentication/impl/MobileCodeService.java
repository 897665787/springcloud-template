package com.company.auth.authentication.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.company.auth.authentication.LoginBeanFactory;
import com.company.auth.authentication.LoginService;
import com.company.auth.authentication.dto.LoginResult;
import com.company.auth.authentication.impl.tool.IOauthTool;
import com.company.common.exception.BusinessException;
import com.company.user.api.enums.UserOauthEnum;

@Component(LoginBeanFactory.MOBILE_CODE)
public class MobileCodeService implements LoginService {

	@Autowired
	private IOauthTool oauthTool;

	@Override
	public LoginResult login(String mobile, String code, String ignore1) {
		if (StringUtils.isBlank(mobile) || StringUtils.isBlank(code)) {
			throw new BusinessException("缺失参数");
		}
		// 核对验证码
		// select * from bu_verify_code where mobile = '15220163215'
		String verifyCode = "123456";
		if (verifyCode == null || !verifyCode.equals(code)) {
			throw new BusinessException("验证码错误");
		}

		Integer userId = oauthTool.getUserIdCertificate(UserOauthEnum.IdentityType.MOBILE, mobile).getUserId();
		if (userId == null) {// 账号不存在
			// 创建新用户
			userId = oauthTool.registerUser(mobile, null, null);
		}
		
		return new LoginResult().setSuccess(true).setUserId(userId);
	}
}
