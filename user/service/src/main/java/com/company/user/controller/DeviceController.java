package com.company.user.controller;

import cn.hutool.core.date.LocalDateTimeUtil;
import com.company.common.api.Result;
import com.company.user.api.feign.DeviceFeign;
import com.company.user.entity.DeviceInfo;
import com.company.user.mapper.user.DeviceInfoMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@RestController
@RequestMapping("/device")
public class DeviceController implements DeviceFeign {

    @Autowired
    private DeviceInfoMapper deviceInfoMapper;


    @Value("${template.device.onlineSeconds:30}")
    private Integer onlineSeconds;// 判断n秒内活跃就算在线

    @Override
    public Result<Boolean> isOnline(String deviceid) {
        if (StringUtils.isBlank(deviceid)) {
            return Result.success(false);
        }

        DeviceInfo deviceInfo = deviceInfoMapper.selectByDeviceid(deviceid);
        if (deviceInfo == null) {
            return Result.success(false);
        }
        long seconds = LocalDateTimeUtil.between(deviceInfo.getTime(), LocalDateTime.now(), ChronoUnit.SECONDS);
        if (seconds > onlineSeconds) {
            return Result.success(false);
        }

        return Result.success(true);
    }
}
