package com.company.tool.service;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.company.tool.entity.SubscribeTemplate;
import com.company.tool.mapper.SubscribeTemplateMapper;

@Service
public class SubscribeTemplateService extends ServiceImpl<SubscribeTemplateMapper, SubscribeTemplate> {

	public SubscribeTemplate selectByPriTmplId(String templateCode) {
		return baseMapper.selectByPriTmplId(templateCode);
	}
}
