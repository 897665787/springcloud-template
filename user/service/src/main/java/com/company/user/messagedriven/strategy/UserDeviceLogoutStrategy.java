package com.company.user.messagedriven.strategy;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import com.company.framework.constant.HeaderConstants;
import com.company.framework.messagedriven.BaseStrategy;
import com.company.user.mapper.user.UserDeviceMapper;

import cn.hutool.core.date.LocalDateTimeUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 绑定用户与设备关系（使用场景：记录登出时间）
 */
@Slf4j
@Component(StrategyConstants.USERDEVICE_LOGOUT_STRATEGY)
public class UserDeviceLogoutStrategy implements BaseStrategy<Map<String, Object>> {

	@Autowired
	private UserDeviceMapper userDeviceMapper;

	@Override
	public void doStrategy(Map<String, Object> params) {
		Integer userId = MapUtils.getInteger(params, "userId");
		if (userId == null) {
			// 没有userId，不处理
			return;
		}

		String timeStr = MapUtils.getString(params, "time");
		LocalDateTime time = LocalDateTimeUtil.parse(timeStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

		@SuppressWarnings("unchecked")
		Map<String, String> httpContextHeader = (Map<String, String>) MapUtils.getMap(params, "httpContextHeader");
		String deviceid = httpContextHeader.get(HeaderConstants.HEADER_DEVICEID);
		if (StringUtils.isBlank(deviceid)) {
			// 没有deviceid，不处理
			return;
		}

		userDeviceMapper.saveOrUpdateLogout(userId, deviceid, time);
	}
}
