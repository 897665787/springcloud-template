package com.company.user.messagedriven.strategy;

import java.util.Map;

import org.apache.commons.collections.MapUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import com.company.framework.messagedriven.BaseStrategy;
import com.company.user.api.enums.UserOauthEnum;
import com.company.user.mapper.user.UserInfoMapper;

/**
 * 绑定邮箱
 */
@Component(StrategyConstants.BINDEMAIL_STRATEGY)
public class BindEmailStrategy implements BaseStrategy<Map<String, Object>> {

	@Autowired
	private UserInfoMapper userInfoMapper;

	@Override
	public void doStrategy(Map<String, Object> params) {
		String identityTypeCode = MapUtils.getString(params, "identityType");
		UserOauthEnum.IdentityType identityType = UserOauthEnum.IdentityType.of(identityTypeCode);
		if (identityType != UserOauthEnum.IdentityType.EMAIL) {
			return;
		}

		Integer userId = MapUtils.getInteger(params, "userId");
		String email = MapUtils.getString(params, "identifier");

//		userInfoMapper.updateEmailById(userId, email);
	}
}
