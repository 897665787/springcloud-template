package com.company.user.amqp.strategy;

import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.company.framework.amqp.BaseStrategy;
import com.company.user.api.enums.UserOauthEnum;
import com.company.user.mapper.user.UserInfoMapper;

/**
 * 绑定手机号
 */
@Component(StrategyConstants.BINDMOBILE_STRATEGY)
public class BindMobileStrategy implements BaseStrategy<Map<String, Object>> {

	@Autowired
	private UserInfoMapper userInfoMapper;

	@Override
	public void doStrategy(Map<String, Object> params) {
		String identityTypeCode = MapUtils.getString(params, "identityType");
		UserOauthEnum.IdentityType identityType = UserOauthEnum.IdentityType.of(identityTypeCode);
		if (identityType != UserOauthEnum.IdentityType.MOBILE) {
			return;
		}

		Integer userId = MapUtils.getInteger(params, "userId");

		String identifier = MapUtils.getString(params, "identifier");
//		String[] arr = MccUtil.split(identifier);
//		String mcc = arr[0];
//		String mobile = arr[1];

//		userInfoMapper.updateMobileById(userId, mcc, mobile);
	}
}
