package com.company.tool.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.company.tool.entity.SubscribeTypeTemplateConfig;
import com.company.tool.mapper.SubscribeTypeTemplateConfigMapper;

@Service
public class SubscribeTypeTemplateConfigService extends ServiceImpl<SubscribeTypeTemplateConfigMapper, SubscribeTypeTemplateConfig> {

	public SubscribeTypeTemplateConfig selectTemplateCodeByType(String type) {
		return baseMapper.selectTemplateCodeByType(type);
	}
	
	public List<String> selectTypeByTemplateCode(String templateCode) {
		return baseMapper.selectTypeByTemplateCode(templateCode);
	}
}
