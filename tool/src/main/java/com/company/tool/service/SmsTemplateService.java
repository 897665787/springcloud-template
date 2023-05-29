package com.company.tool.service;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.company.tool.entity.SmsTemplate;
import com.company.tool.mapper.SmsTemplateMapper;

@Service
public class SmsTemplateService extends ServiceImpl<SmsTemplateMapper, SmsTemplate> {

	public SmsTemplate selectByChannelTemplateCode(String channel, String templateCode) {
		return baseMapper.selectByChannelTemplateCode(channel, templateCode);
	}
}
