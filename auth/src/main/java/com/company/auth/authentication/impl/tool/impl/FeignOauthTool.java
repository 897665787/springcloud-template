package com.company.auth.authentication.impl.tool.impl;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.company.auth.authentication.impl.tool.IOauthTool;
import com.company.auth.authentication.impl.tool.dto.MobileBindAuthCode;
import com.company.auth.authentication.impl.tool.dto.UserIdCertificate;
import com.company.framework.cache.ICache;
import com.company.user.api.enums.UserOauthEnum;
import com.company.user.api.feign.UserInfoFeign;
import com.company.user.api.feign.UserOauthFeign;
import com.company.user.api.request.UserInfoReq;
import com.company.user.api.request.UserOauthReq;
import com.company.user.api.response.UserInfoResp;
import com.company.user.api.response.UserOauthResp;

@Component
public class FeignOauthTool implements IOauthTool {

	@Autowired
	private UserInfoFeign userInfoFeign;
	@Autowired
	private UserOauthFeign userOauthFeign;
	@Autowired
	private ICache cache;

	@Override
	public UserIdCertificate getUserIdCertificate(UserOauthEnum.IdentityType identityType, String identifier) {
		UserOauthResp userOauthResp = userOauthFeign.selectOauth(identityType, identifier).dataOrThrow();
		if (userOauthResp == null) {
			return new UserIdCertificate();
		}
		return new UserIdCertificate().setUserId(userOauthResp.getUserId())
				.setCertificate(userOauthResp.getCertificate());
	}

	@Override
	public void bindOauth(Integer userId, UserOauthEnum.IdentityType identityType, String identifier, String certificate) {
		UserOauthReq userInfoReq = new UserOauthReq();
		userInfoReq.setUserId(userId);
		userInfoReq.setIdentityType(identityType);
		userInfoReq.setIdentifier(identifier);
		userInfoReq.setCertificate(certificate);
		userOauthFeign.bindOauth(userInfoReq);
	}

	@Override
	public void storeMobileBindAuthCode(String authcode, MobileBindAuthCode mobileBindAuthCode) {
		cache.set("authcode:" + authcode, mobileBindAuthCode, 1, TimeUnit.HOURS);
	}

	@Override
	public MobileBindAuthCode getMobileBindAuthCode4Store(String authcode) {
		MobileBindAuthCode mobileBindAuthCode = cache.get("authcode:" + authcode, MobileBindAuthCode.class);
		return mobileBindAuthCode;
	}

	@Override
	public Integer registerUser(String mobile, String nickname, String avator) {
		UserInfoReq userInfoReq = new UserInfoReq();
		userInfoReq.setMobile(mobile);
		userInfoReq.setNickname(nickname);
		userInfoReq.setAvator(avator);
		UserInfoResp userInfoResp = userInfoFeign.findOrCreateUser(userInfoReq).dataOrThrow();
		
		Integer userId = userInfoResp.getId();
		return userId;
	}
}
