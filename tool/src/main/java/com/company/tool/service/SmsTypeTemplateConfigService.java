package com.company.tool.service;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.company.tool.entity.SmsTypeTemplateConfig;
import com.company.tool.mapper.SmsTypeTemplateConfigMapper;

@Service
public class SmsTypeTemplateConfigService extends ServiceImpl<SmsTypeTemplateConfigMapper, SmsTypeTemplateConfig> {

	public SmsTypeTemplateConfig selectByTypeChannel(String type, String channel) {
		return baseMapper.selectByTypeChannel(type, channel);
	}
}
