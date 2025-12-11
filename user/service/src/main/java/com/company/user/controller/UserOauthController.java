package com.company.user.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.company.common.api.Result;
import com.company.framework.util.PropertyUtils;
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
	public Result<UserOauthResp> selectOauth(UserOauthEnum.IdentityType identityType, String identifier) {

		UserOauth userOauth = userOauthMapper.selectByIdentityTypeIdentifier(identityType, identifier);
		return Result.success(PropertyUtils.copyProperties(userOauth, UserOauthResp.class));
	}

	@Override
	public Result<String> selectIdentifier(Integer userId, UserOauthEnum.IdentityType identityType) {
		UserOauth userOauth = userOauthMapper.selectByUserIdIdentityType(userId, identityType);
		return Result.success(userOauth.getIdentifier());
	}

    @Override
    public Result<String> selectPassword(Integer userId) {
        UserOauth userOauth = userOauthMapper.selectByUserIdIdentityType(userId, UserOauthEnum.IdentityType.PASSWORD);
        return Result.success(userOauth.getCertificate());
    }

	@Override
	public Result<Boolean> bindOauth(@RequestBody @Valid UserOauthReq userInfoReq) {

		userOauthMapper.bindOauth(userInfoReq.getUserId(), userInfoReq.getIdentityType(), userInfoReq.getIdentifier(),
				userInfoReq.getCertificate());

		return Result.success(true);
	}
}
