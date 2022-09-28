package com.company.auth.authentication.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.company.auth.authentication.LoginBeanFactory;
import com.company.auth.authentication.LoginService;
import com.company.auth.authentication.dto.LoginResult;
import com.company.auth.authentication.impl.tool.IMaMobileTool;
import com.company.auth.authentication.impl.tool.IOauthTool;
import com.company.auth.authentication.impl.tool.dto.MaMobile;
import com.company.common.exception.BusinessException;
import com.company.user.api.enums.UserOauthEnum;

@Component(LoginBeanFactory.WEIXIN_MINIAPP_MOBILE)
public class WeixinMiniappMobileService implements LoginService {

	@Value("${appid.wx.miniapp:wxeb6ffb1ebd72a4fd1}")
	private String appid;
	
	@Autowired
	private IOauthTool oauthTool;
	@Autowired
	private IMaMobileTool maMobileTool;
	
	@Override
	public LoginResult login(String ignore1, String ignore2, String code) {
		if (StringUtils.isBlank(code)) {
			throw new BusinessException("缺失参数");
		}

		MaMobile maMobile = maMobileTool.getMobileInfo(appid, code);
		
		Integer errcode = maMobile.getErrcode();
		if (errcode != null && errcode != 0) {
			// 授权失败
			return new LoginResult().setSuccess(false).setMessage(maMobile.getErrmsg());
		}

		String mobile = maMobile.getPhoneNumber();
		if (StringUtils.isBlank(mobile)) {
			// 授权失败
			return new LoginResult().setSuccess(false).setMessage("授权失败");
		}
		
		Integer userId = oauthTool.getUserIdCertificate(UserOauthEnum.IdentityType.MOBILE, mobile).getUserId();
		if (userId == null) {// 账号不存在
			// 创建新用户
			userId = oauthTool.registerUser(mobile, null, null);
		}
		return new LoginResult().setSuccess(true).setUserId(userId);
	}
}
