package com.company.auth.authentication.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.company.auth.authentication.LoginBeanFactory;
import com.company.auth.authentication.LoginService;
import com.company.auth.authentication.dto.LoginResult;
import com.company.auth.authentication.impl.tool.IOauthTool;
import com.company.auth.authentication.impl.tool.dto.UserIdCertificate;
import com.company.common.exception.BusinessException;
import com.company.user.api.enums.UserOauthEnum;

@Component(LoginBeanFactory.USERNAME_PASSWORD)
public class UsernamePasswordService implements LoginService {

	@Autowired
	private IOauthTool oauthTool;

	@Override
	public LoginResult login(String username, String password, String ignore1) {
		if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
			throw new BusinessException("缺失参数");
		}

		// 根据username查询password，比较
		UserIdCertificate userIdCertificate = oauthTool.getUserIdCertificate(UserOauthEnum.IdentityType.USERNAME,
				username);
		Integer userId = userIdCertificate.getUserId();
		if (userId == null) {
			throw new BusinessException("用户名错误");
		}
		if (StringUtils.isBlank(userIdCertificate.getCertificate())) {
			throw new BusinessException("未设置密码");
		}
		if (!userIdCertificate.getCertificate().equals(password)) {
			throw new BusinessException("密码错误");
		}
		return new LoginResult().setSuccess(true).setUserId(userId);
	}
}
