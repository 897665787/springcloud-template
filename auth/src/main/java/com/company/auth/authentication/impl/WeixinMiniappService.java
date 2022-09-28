package com.company.auth.authentication.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.company.auth.authentication.LoginBeanFactory;
import com.company.auth.authentication.LoginService;
import com.company.auth.authentication.dto.LoginResult;
import com.company.auth.authentication.impl.tool.IMaTool;
import com.company.auth.authentication.impl.tool.IOauthTool;
import com.company.auth.authentication.impl.tool.dto.MaSession;
import com.company.auth.authentication.impl.tool.dto.MaSessionPhoneNumber;
import com.company.common.exception.BusinessException;
import com.company.user.api.enums.UserOauthEnum;

@Component(LoginBeanFactory.WEIXIN_MINIAPP)
public class WeixinMiniappService implements LoginService {

	@Value("${appid.wx.miniapp:wxeb6ffb1ebd72a4fd1}")
	private String appid;
	
	@Autowired
	private IOauthTool oauthTool;
	@Autowired
	private IMaTool maTool;
	
	@Override
	public LoginResult login(String encryptedData, String iv, String wxcode) {
		if (StringUtils.isBlank(encryptedData) || StringUtils.isBlank(iv) || StringUtils.isBlank(wxcode)) {
			throw new BusinessException("缺失参数");
		}

		MaSessionPhoneNumber maSessionPhoneNumber = maTool.getSessionInfoAndPhoneNumber(appid, encryptedData, iv, wxcode);
		MaSession maSession = maSessionPhoneNumber.getMaSession();
		
		Integer errcode = maSession.getErrcode();
		if (errcode != null && errcode != 0) {
			// 授权失败
			return new LoginResult().setSuccess(false).setMessage(maSession.getErrmsg());
		}

		String mobile = maSessionPhoneNumber.getPhoneNumber();
		if (StringUtils.isBlank(mobile)) {
			// 授权失败
			return new LoginResult().setSuccess(false).setMessage("授权失败");
		}
		
		Integer userId = oauthTool.getUserIdCertificate(UserOauthEnum.IdentityType.MOBILE, mobile).getUserId();
		if (userId == null) {// 账号不存在
			// 创建新用户
			userId = oauthTool.registerUser(mobile, null, null);
			
			// 绑定用户与openid,unionid关系
			String openid = maSession.getOpenid();
			oauthTool.bindOauth(userId, UserOauthEnum.IdentityType.WX_OPENID_MINIAPP, openid, wxcode);
			String unionid = maSession.getUnionid();
			if (StringUtils.isNotBlank(unionid)) {
				oauthTool.bindOauth(userId, UserOauthEnum.IdentityType.WX_UNIONID, unionid, wxcode);
			}
			
		}
		return new LoginResult().setSuccess(true).setUserId(userId);
	}
}
