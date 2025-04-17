package com.company.user.messagedriven.strategy;

import cn.hutool.core.date.LocalDateTimeUtil;
import com.company.framework.cache.ICache;
import com.company.framework.messagedriven.BaseStrategy;
import com.company.user.mapper.user.DeviceInfoMapper;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 记录设备信息
 */
@Component(StrategyConstants.DEVICEINFORECORD_STRATEGY)
public class DeviceInfoRecordStrategy implements BaseStrategy<Map<String, Object>> {

	private static final String EXIST_VALUE = "1";

	@Autowired
	private ICache cache;
	@Autowired
	private DeviceInfoMapper deviceInfoMapper;

	@Override
	public void doStrategy(Map<String, Object> params) {
		String deviceid = MapUtils.getString(params, "deviceid");
		String platform = MapUtils.getString(params, "platform");
		String operator = MapUtils.getString(params, "operator");
		String channel = MapUtils.getString(params, "channel");
		String version = MapUtils.getString(params, "version");
		String requestip = MapUtils.getString(params, "requestip");
		String userAgent = MapUtils.getString(params, "userAgent");

		// 数据量可能很大，需要快速过滤重复的数据，加快处理速度
		String key = String.format("device_info:%s", deviceid);
		String result = cache.get(key);
		if (EXIST_VALUE.equals(result)) {
			return;
		}

		// 保存deviceid、platform、operator、channel、version、requestip、userAgent关联关系到DB
		String timeStr = MapUtils.getString(params, "time");
		LocalDateTime time = LocalDateTimeUtil.parse(timeStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
		save2db(deviceid, platform, operator, channel, version, requestip, userAgent, time);

		// 数据量可能很大，需要快速过滤重复的数据，加快处理速度
		cache.set(key, EXIST_VALUE, 30, TimeUnit.SECONDS);
	}

	private void save2db(String deviceid, String platform, String operator, String channel, String version, String requestip, String userAgent
			, LocalDateTime time) {
		deviceInfoMapper.saveOrIgnore(deviceid, platform, operator, channel, version, requestip, userAgent, time);
	}
}
