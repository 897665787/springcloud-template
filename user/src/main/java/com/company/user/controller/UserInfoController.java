package com.company.user.controller;

import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.company.common.api.Result;
import com.company.framework.amqp.MessageSender;
import com.company.framework.amqp.rabbit.constants.FanoutConstants;
import com.company.framework.redis.redisson.DistributeLockUtils;
import com.company.user.api.enums.UserOauthEnum;
import com.company.user.api.feign.UserInfoFeign;
import com.company.user.api.request.UserInfoReq;
import com.company.user.api.response.UserInfoResp;
import com.company.user.entity.UserInfo;
import com.company.user.entity.UserOauth;
import com.company.user.mapper.user.UserInfoMapper;
import com.company.user.mapper.user.UserOauthMapper;
import com.google.common.collect.Maps;

@RestController
@RequestMapping("/userinfo")
public class UserInfoController implements UserInfoFeign {

	@Autowired
	private UserInfoMapper userInfoMapper;
	@Autowired
	private UserOauthMapper userOauthMapper;
	@Autowired
	private MessageSender messageSender;

	/**
	 * <pre>
	 * 1.如果手机号存在，直接返回已绑定的用户ID
	 * 2.如果手机号不存在，新增用户并绑定手机号，返回新增用户ID
	 * </pre>
	 */
	@Override
	public Result<UserInfoResp> findOrCreateUser(@RequestBody @Valid UserInfoReq userInfoReq) {
		String mobile = userInfoReq.getMobile();
		
		UserOauth userOauthDB = userOauthMapper.selectByIdentityTypeIdentifier(UserOauthEnum.IdentityType.MOBILE, mobile);
		if (userOauthDB != null) {
			UserInfoResp userInfoResp = new UserInfoResp().setId(userOauthDB.getId());
			return Result.success(userInfoResp);
		}
		
		String key = String.format("lock:register:%s", mobile);
		Integer userId0 = DistributeLockUtils.doInDistributeLockThrow(key, () -> {
			UserOauth userOauth = userOauthMapper.selectByIdentityTypeIdentifier(UserOauthEnum.IdentityType.MOBILE, mobile);
			if (userOauth != null) {
				return userOauth.getUserId();
			}

			UserInfo userInfo = new UserInfo().setNickname(userInfoReq.getNickname())
					.setAvator(userInfoReq.getAvator());
			userInfoMapper.insert(userInfo);

			userOauthMapper.bindOauth(userInfo.getId(), UserOauthEnum.IdentityType.MOBILE, mobile, null);

			// 发布注册事件
			Map<String, Object> params = Maps.newHashMap();
			params.put("userId", userInfo.getId());
			params.put("mobile", mobile);
			params.put("nickname", userInfo.getNickname());
			params.put("avator", userInfo.getAvator());
			messageSender.sendFanoutMessage(params, FanoutConstants.USER_REGISTER.EXCHANGE);

			return userInfo.getId();
		});

		UserInfoResp userInfoResp = new UserInfoResp().setId(userId0);

		return Result.success(userInfoResp);
	}
}
