package com.company.tool.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.company.tool.entity.AppVersion;
import com.company.tool.entity.SubscribeTask;
import com.company.tool.mapper.AppVersionMapper;
import com.company.tool.mapper.SubscribeTaskMapper;
import org.springframework.stereotype.Service;

@Service
public class AppVersionService extends ServiceImpl<AppVersionMapper, AppVersion> {

    public AppVersion selectLastByAppCode(String appCode) {
        return baseMapper.selectLastByAppCode(appCode);
    }
}
