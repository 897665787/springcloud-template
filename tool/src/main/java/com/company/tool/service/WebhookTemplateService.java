package com.company.tool.service;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.company.tool.entity.WebhookTemplate;
import com.company.tool.mapper.WebhookTemplateMapper;

@Service
public class WebhookTemplateService extends ServiceImpl<WebhookTemplateMapper, WebhookTemplate> {

	public WebhookTemplate selectByType(String type) {
		return baseMapper.selectByType(type);
	}
}
