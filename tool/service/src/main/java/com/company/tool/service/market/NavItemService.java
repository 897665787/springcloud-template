package com.company.tool.service.market;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.company.tool.api.enums.NavItemEnum;
import com.company.tool.entity.NavItem;
import com.company.tool.mapper.market.NavItemMapper;

@Component
public class NavItemService extends ServiceImpl<NavItemMapper, NavItem> implements IService<NavItem> {

	public List<NavItem> selectValidOrderby(NavItemEnum.Status status, LocalDateTime now) {
		return baseMapper.selectValidOrderby(status, now);
	}
}
