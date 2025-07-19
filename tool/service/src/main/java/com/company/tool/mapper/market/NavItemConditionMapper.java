package com.company.tool.mapper.market;

import java.util.Collection;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.company.tool.entity.NavItemCondition;

public interface NavItemConditionMapper extends BaseMapper<NavItemCondition> {
	List<NavItemCondition> selectByNavItemIds(@Param("navItemIds") Collection<Integer> navItemIds);
}
