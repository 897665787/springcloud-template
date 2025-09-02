package com.company.tool.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.company.tool.entity.AppVersion;
import com.company.tool.entity.SubscribeTask;
import com.company.tool.mapper.AppVersionMapper;
import com.company.tool.mapper.SubscribeTaskMapper;
import com.github.smartretry.core.RetryFunction;
import org.springframework.stereotype.Service;

@Service
public class AppVersionService extends ServiceImpl<AppVersionMapper, AppVersion> {
    @RetryFunction(identity = "AppVersion.selectLastByAppCode", interval = 5, maxRetryCount = 10)
    public AppVersion selectLastByAppCode(String appCode) {
        if (true) {
            throw new RuntimeException("error22222");
        }
        return baseMapper.selectLastByAppCode(appCode);
    }
}
