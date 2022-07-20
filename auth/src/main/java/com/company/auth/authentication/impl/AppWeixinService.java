package com.company.auth.authentication.impl;

import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.company.auth.authentication.LoginBeanFactory;
import com.company.auth.authentication.LoginService;
import com.company.auth.authentication.dto.LoginResult;
import com.company.auth.authentication.impl.tool.IMpTool;
import com.company.auth.authentication.impl.tool.IOauthTool;
import com.company.auth.authentication.impl.tool.dto.MobileBindAuthCode;
import com.company.auth.authentication.impl.tool.dto.MobileBindAuthCode.BindUserOauth;
import com.company.auth.authentication.impl.tool.dto.MpAccessToken;
import com.company.auth.authentication.impl.tool.dto.MpUserInfo;
import com.company.common.exception.BusinessException;
import com.company.user.api.enums.UserOauthEnum;
import com.google.common.collect.Lists;

/**
 * <pre>
 * 官网：https://developers.weixin.qq.com/doc/oplatform/Mobile_App/WeChat_Login/Development_Guide.html
 * </pre>
 * 
 * @author JQ棣
 *
 */
@Component(LoginBeanFactory.APP_WEIXIN)
public class AppWeixinService implements LoginService {
	
	@Value("${app.wx.appid:wxeb6ffb1ebd72a4fds}")
	private String appid;
	
	@Autowired
	private IMpTool mpTool;
	
	@Autowired
	private IOauthTool oauthTool;
	
	@Override
	public LoginResult login(String ignore1, String ignore2, String wxcode) {
		if (StringUtils.isBlank(wxcode)) {
			throw new BusinessException("缺失参数");
		}
		
        MpAccessToken mpAccessToken = mpTool.getAccessToken(appid, wxcode);
		
		Integer errcode = mpAccessToken.getErrcode();
		if (errcode != null && errcode != 0) {
			// 授权失败
			return new LoginResult().setSuccess(false).setMessage("授权失败");
		}

		String openid = mpAccessToken.getOpenid();
		if (StringUtils.isBlank(openid)) {
			// 授权失败
			return new LoginResult().setSuccess(false).setMessage("授权失败");
		}
		
		String accessToken = mpAccessToken.getAccessToken();
		
		String unionid = mpAccessToken.getUnionid();// 可能没有unionid，需要通过userinfo获取
		MpUserInfo userinfo = null;
		if (StringUtils.isBlank(unionid)) {
			userinfo = mpTool.getUserinfo(accessToken, openid);
			unionid = userinfo.getUnionid();
		}
		
		// 根据openid查询user_id
		Integer userId = oauthTool.getUserIdCertificate(UserOauthEnum.IdentityType.WX_OPENID_APP, openid).getUserId();
		if (userId == null) {
			// 根据unionid查询user_id
			if (StringUtils.isNotBlank(unionid)) {// 未绑定微信开放平台是没有unionid的
				userId = oauthTool.getUserIdCertificate(UserOauthEnum.IdentityType.WX_UNIONID, unionid).getUserId();
			}
			if (userId != null) {
				// unionid已经找到用户，但是openid没找到用户，自动做绑定
				oauthTool.bindOauth(userId, UserOauthEnum.IdentityType.WX_OPENID_APP, openid, wxcode);
			}
		} else {
			// openid已经找到用户，如果unionid没找到用户的话自动做绑定
			if (StringUtils.isNotBlank(unionid)) {// 未绑定微信开放平台是没有unionid的
				Integer userId2 = oauthTool.getUserIdCertificate(UserOauthEnum.IdentityType.WX_UNIONID, unionid).getUserId();
				if (userId2 == null) {
					oauthTool.bindOauth(userId, UserOauthEnum.IdentityType.WX_UNIONID, unionid, wxcode);
				}
			}
		}
		
		if (userId == null) {
			// 说明微信没有绑定账号，需前端跳转到手机号+验证码登录(MOBILE_CODE_BIND)，并携带信息可以方便后续找到openid，unionid和用户信息
			if (userinfo == null) {
				userinfo = mpTool.getUserinfo(accessToken, openid);
			}
			// 存储MobileBindAuthCode相关信息
			MobileBindAuthCode mobileBindAuthCode = new MobileBindAuthCode();
			mobileBindAuthCode.setNickname(Optional.ofNullable(userinfo).map(MpUserInfo::getNickname).orElse(null));
			mobileBindAuthCode.setHeadimgurl(Optional.ofNullable(userinfo).map(MpUserInfo::getHeadimgurl).orElse(null));
			List<BindUserOauth> binds = Lists.newArrayList();
			binds.add(new BindUserOauth().setIdentityType(UserOauthEnum.IdentityType.WX_OPENID_MINIAPP)
					.setIdentifier(openid));
			binds.add(
					new BindUserOauth().setIdentityType(UserOauthEnum.IdentityType.WX_UNIONID).setIdentifier(unionid));
			mobileBindAuthCode.setBinds(binds);
			oauthTool.storeMobileBindAuthCode(wxcode, mobileBindAuthCode);
				
			return new LoginResult().setSuccess(false).setMessage("微信没有绑定账号");
		}

		// 通过解密微信的密文获取手机号码，可直接使用
		return new LoginResult().setSuccess(true).setUserId(userId);
	}
}
