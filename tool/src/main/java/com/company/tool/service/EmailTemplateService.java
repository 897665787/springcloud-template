package com.company.tool.service;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.company.tool.entity.EmailTemplate;
import com.company.tool.mapper.EmailTemplateMapper;

@Service
public class EmailTemplateService extends ServiceImpl<EmailTemplateMapper, EmailTemplate> {

	public EmailTemplate selectByType(String type) {
		return baseMapper.selectByType(type);
	}
}
