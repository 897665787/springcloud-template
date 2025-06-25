package com.company.user.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.company.common.util.PropertyUtils;
import com.company.user.api.enums.UserOauthEnum;
import com.company.user.api.feign.UserOauthFeign;
import com.company.user.api.request.UserOauthReq;
import com.company.user.api.response.UserOauthResp;
import com.company.user.entity.UserOauth;
import com.company.user.mapper.user.UserOauthMapper;

@RestController
@RequestMapping("/useroauth")
public class UserOauthController implements UserOauthFeign {

	@Autowired
	private UserOauthMapper userOauthMapper;

	@Override
	public UserOauthResp selectOauth(UserOauthEnum.IdentityType identityType, String identifier) {

		UserOauth userOauth = userOauthMapper.selectByIdentityTypeIdentifier(identityType, identifier);
		return PropertyUtils.copyProperties(userOauth, UserOauthResp.class);
	}

	@Override
	public String selectIdentifier(Integer userId, UserOauthEnum.IdentityType identityType) {
		UserOauth userOauth = userOauthMapper.selectByUserIdIdentityType(userId, identityType);
		return userOauth.getIdentifier();
	}

	@Override
	public String selectCertificate(Integer userId, UserOauthEnum.IdentityType identityType) {
		UserOauth userOauth = userOauthMapper.selectByUserIdIdentityType(userId, identityType);
		return userOauth.getCertificate();
	}

	@Override
	public Boolean bindOauth(@RequestBody @Valid UserOauthReq userInfoReq) {

		userOauthMapper.bindOauth(userInfoReq.getUserId(), userInfoReq.getIdentityType(), userInfoReq.getIdentifier(),
				userInfoReq.getCertificate());

		return true;
	}
}
