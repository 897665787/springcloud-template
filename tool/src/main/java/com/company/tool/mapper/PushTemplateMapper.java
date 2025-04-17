package com.company.tool.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.company.tool.entity.PushTemplate;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface PushTemplateMapper extends BaseMapper<PushTemplate> {

	@Select("select * from push_template where type = #{type}")
	PushTemplate selectByType(@Param("type") String type);
}