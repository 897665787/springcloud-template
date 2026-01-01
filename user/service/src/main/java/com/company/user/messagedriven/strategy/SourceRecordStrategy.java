package com.company.user.messagedriven.strategy;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.collections.MapUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.company.framework.cache.ICache;
import com.company.framework.messagedriven.BaseStrategy;
import com.company.user.entity.UserSource;
import com.company.user.mapper.user.UserSourceMapper;

import cn.hutool.core.date.LocalDateTimeUtil;

/**
 * 记录来源（使用场景：引流统计、邀请奖励、地推业绩计算等业务场景）
 */
@Component(StrategyConstants.SOURCERECORD_STRATEGY)
public class SourceRecordStrategy implements BaseStrategy<Map<String, Object>> {

	private static final String EXIST_VALUE = "1";

	@Autowired
	private ICache cache;
	@Autowired
	private UserSourceMapper userSourceMapper;

	@Value("${template.timeoutSeconds.sourceRecord:1800}")
	private Long timeoutSeconds;

	@Override
	public void doStrategy(Map<String, Object> params) {
		String source = MapUtils.getString(params, "source");
		String deviceid = MapUtils.getString(params, "deviceid");

		// 数据量可能很大，需要快速过滤重复的数据，加快处理速度
		String key = String.format("user_source:%s:%s", source, deviceid);
		String result = cache.get(key);
		if (EXIST_VALUE.equals(result)) {
			return;
		}

		// 保存source、deviceid关联关系到DB
		String timeStr = MapUtils.getString(params, "time");
		LocalDateTime time = LocalDateTimeUtil.parse(timeStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
		save2db(deviceid, source, time);

		// 数据量可能很大，需要快速过滤重复的数据，加快处理速度
		cache.set(key, EXIST_VALUE, timeoutSeconds, TimeUnit.SECONDS);
	}

	private void save2db(String deviceid, String source, LocalDateTime time) {
		// 获取deviceid最近1次记录
		UserSource lastUserSource = userSourceMapper.selectLastByDeviceid(deviceid);
		if (lastUserSource != null) {
			if (source.equals(lastUserSource.getSource())) {
				return;
			}
		}

		userSourceMapper.saveOrIgnore(deviceid, source, time);
	}
}
