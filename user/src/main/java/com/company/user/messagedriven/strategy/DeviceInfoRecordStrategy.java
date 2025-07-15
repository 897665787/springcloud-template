package com.company.user.messagedriven.strategy;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.company.framework.cache.ICache;
import com.company.framework.messagedriven.BaseStrategy;
import com.company.user.entity.DeviceInfo;
import com.company.user.mapper.user.DeviceInfoMapper;

import cn.hutool.core.date.LocalDateTimeUtil;

/**
 * 记录设备信息（使用场景：推送）
 */
@Component(StrategyConstants.DEVICEINFORECORD_STRATEGY)
public class DeviceInfoRecordStrategy implements BaseStrategy<Map<String, Object>> {

    private static final String EXIST_VALUE = "1";

    @Autowired
    private ICache cache;
    @Autowired
    private DeviceInfoMapper deviceInfoMapper;

    @Value("${template.timeoutSeconds.deviceInfo:30}")
    private Long timeoutSeconds;

    @Override
    public void doStrategy(Map<String, Object> params) {
        String deviceid = MapUtils.getString(params, "deviceid");
        if (StringUtils.isBlank(deviceid)) {
            // 没有deviceid，不处理
            return;
        }
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
        cache.set(key, EXIST_VALUE, timeoutSeconds, TimeUnit.SECONDS);
    }

    private void save2db(String deviceid, String platform, String operator, String channel, String version, String requestip, String userAgent, LocalDateTime time) {
        // 获取记录
        DeviceInfo deviceInfo = deviceInfoMapper.selectByDeviceid(deviceid);
        if (deviceInfo == null) {// 新增
            deviceInfoMapper.saveOrUpdate(deviceid, platform, operator, channel, version, requestip, userAgent, time);
            return;
        }
        if (time.isBefore(deviceInfo.getTime())) { // 时间小于原来的时间，说明是旧数据，不需要更新
            return;
        }
        // 更新有值的字段
        DeviceInfo deviceInfo4Update = new DeviceInfo();
        deviceInfo4Update.setId(deviceInfo.getId());
        if (StringUtils.isNotBlank(platform)) {
            deviceInfo4Update.setPlatform(platform);
        }
        if (StringUtils.isNotBlank(operator)) {
            deviceInfo4Update.setOperator(operator);
        }
        if (StringUtils.isNotBlank(channel)) {
            deviceInfo4Update.setChannel(channel);
        }
        if (StringUtils.isNotBlank(version)) {
            deviceInfo4Update.setVersion(version);
        }
        if (StringUtils.isNotBlank(requestip)) {
            deviceInfo4Update.setRequestip(requestip);
        }
        if (StringUtils.isNotBlank(userAgent)) {
            deviceInfo4Update.setRequestUserAgent(userAgent);
        }
        deviceInfo4Update.setTime(time);
        deviceInfoMapper.updateById(deviceInfo4Update);
    }
}
