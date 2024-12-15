package com.company.tool.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.company.tool.entity.SubscribeGroupType;

public interface SubscribeGroupTypeMapper extends BaseMapper<SubscribeGroupType> {

	@Select("select types from subscribe_group_type where `group` = #{group}")
	String selectByGroup(@Param("group") String group);
}