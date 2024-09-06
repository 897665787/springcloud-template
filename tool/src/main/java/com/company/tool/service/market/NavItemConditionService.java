package com.company.tool.service.market;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.company.tool.entity.NavItemCondition;
import com.company.tool.mapper.market.NavItemConditionMapper;

@Component
public class NavItemConditionService extends ServiceImpl<NavItemConditionMapper, NavItemCondition> implements IService<NavItemCondition> {
	
	public List<NavItemCondition> selectByNavItemIds(Collection<Integer> navItemIds) {
		if (CollectionUtils.isEmpty(navItemIds)) {
			return Collections.emptyList();
		}
		return baseMapper.selectByNavItemIds(navItemIds);
	}
}
