package com.company.user.messagedriven.strategy;

import cn.hutool.core.date.LocalDateTimeUtil;
import com.company.common.constant.HeaderConstants;
import com.company.framework.messagedriven.BaseStrategy;
import com.company.user.mapper.user.UserDeviceMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

/**
 * 绑定用户与设备关系（使用场景：记录登录时间、推送）
 */
@Slf4j
@Component(StrategyConstants.USERDEVICE_LOGIN_STRATEGY)
public class UserDeviceLoginStrategy implements BaseStrategy<Map<String, Object>> {

	@Autowired
	private UserDeviceMapper userDeviceMapper;

	@Override
	public void doStrategy(Map<String, Object> params) {
		Integer userId = MapUtils.getInteger(params, "userId");

		String timeStr = MapUtils.getString(params, "time");
		LocalDateTime time = LocalDateTimeUtil.parse(timeStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

		@SuppressWarnings("unchecked")
		Map<String, String> httpContextHeader = (Map<String, String>) MapUtils.getMap(params, "httpContextHeader");
		String deviceid = httpContextHeader.get(HeaderConstants.HEADER_DEVICEID);

		userDeviceMapper.saveOrUpdateLogin(userId, deviceid, time);
	}
}
