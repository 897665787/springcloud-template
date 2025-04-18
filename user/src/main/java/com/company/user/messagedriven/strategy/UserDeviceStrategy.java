package com.company.user.messagedriven.strategy;

import com.company.common.constant.HeaderConstants;
import com.company.framework.messagedriven.BaseStrategy;
import com.company.user.mapper.user.UserDeviceMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 绑定用户与设备关系（用于推送场景）
 */
@Slf4j
@Component(StrategyConstants.USERDEVICE_STRATEGY)
public class UserDeviceStrategy implements BaseStrategy<Map<String, Object>> {

	@Autowired
	private UserDeviceMapper userDeviceMapper;

	@Override
	public void doStrategy(Map<String, Object> params) {
		Integer userId = MapUtils.getInteger(params, "userId");

		@SuppressWarnings("unchecked")
		Map<String, String> httpContextHeader = (Map<String, String>) MapUtils.getMap(params, "httpContextHeader");
		String deviceid = httpContextHeader.get(HeaderConstants.HEADER_DEVICEID);

		userDeviceMapper.saveOrUpdate(userId, deviceid);
	}
}
