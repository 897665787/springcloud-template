package com.company.tool.service;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.company.tool.entity.PushTemplate;
import com.company.tool.mapper.PushTemplateMapper;

@Service
public class PushTemplateService extends ServiceImpl<PushTemplateMapper, PushTemplate> {

    public PushTemplate selectByType(String type) {
        return baseMapper.selectByType(type);
    }
}
