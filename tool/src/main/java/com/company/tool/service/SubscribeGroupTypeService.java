package com.company.tool.service;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.company.tool.api.enums.SubscribeEnum;
import com.company.tool.entity.SubscribeGroupType;
import com.company.tool.mapper.SubscribeGroupTypeMapper;
import com.google.common.collect.Lists;

@Service
public class SubscribeGroupTypeService extends ServiceImpl<SubscribeGroupTypeMapper, SubscribeGroupType> {

	@Autowired
	private SubscribeTypeTemplateConfigService subscribeTypeTemplateConfigService;
	
	public List<SubscribeEnum.Type> selectTypesByGroup(String group) {
		String types = baseMapper.selectByGroup(group);
		if (StringUtils.isBlank(types)) {
			return Collections.emptyList();
		}
		String[] typeArr = StringUtils.split(types, ",");
		if (typeArr.length == 0) {
			return Collections.emptyList();
		}

		List<SubscribeEnum.Type> typeList = Lists.newArrayList();
		for (String type : typeArr) {
			typeList.add(SubscribeEnum.Type.of(type));
		}
		return typeList;
	}

	public SubscribeEnum.Type selectTypeByGroupTemplateCode(String group, String templateCode) {
		String types = baseMapper.selectByGroup(group);
		if (StringUtils.isBlank(types)) {
			return null;
		}
		
		String[] typeArr = StringUtils.split(types, ",");
		if (typeArr.length == 0) {
			return null;
		}

		List<String> typeList = subscribeTypeTemplateConfigService.selectTypeByTemplateCode(templateCode);
		for (String type : typeArr) {
			if (typeList.contains(type)) {
				return SubscribeEnum.Type.of(type);
			}
		}
		return null;
	}
}
