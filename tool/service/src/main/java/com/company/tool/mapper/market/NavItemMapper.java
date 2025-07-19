package com.company.tool.mapper.market;

import java.time.LocalDateTime;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.company.tool.api.enums.NavItemEnum;
import com.company.tool.entity.NavItem;

public interface NavItemMapper extends BaseMapper<NavItem> {

	@Select("select * from mk_nav_item"
			+ " where"
			+ " begin_time < #{time} and end_time > #{time}"
			+ " and status = #{status.code}"
			+ " order by position asc,priority desc,id desc")
	List<NavItem> selectValidOrderby(@Param("status") NavItemEnum.Status status, @Param("time") LocalDateTime time);
}
