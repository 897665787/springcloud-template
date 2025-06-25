package com.company.user.controller;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.company.user.api.feign.DeviceFeign;
import com.company.user.entity.DeviceInfo;
import com.company.user.mapper.user.DeviceInfoMapper;

import cn.hutool.core.date.LocalDateTimeUtil;

@RestController
@RequestMapping("/userinfo")
public class DeviceController implements DeviceFeign {

    @Autowired
    private DeviceInfoMapper deviceInfoMapper;


    @Value("${template.device.onlineSeconds:30}")
    private Integer onlineSeconds;// 判断n秒内活跃就算在线

    @Override
    public Boolean isOnline(String deviceid) {
        if (StringUtils.isBlank(deviceid)) {
            return false;
        }

        DeviceInfo deviceInfo = deviceInfoMapper.selectByDeviceid(deviceid);
        long seconds = LocalDateTimeUtil.between(deviceInfo.getTime(), LocalDateTime.now(), ChronoUnit.SECONDS);
        if (seconds > onlineSeconds) {
            return false;
        }

        return true;
    }
}
