package com.company.tool.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.company.tool.entity.SmsTypeTemplateConfig;

public interface SmsTypeTemplateConfigMapper extends BaseMapper<SmsTypeTemplateConfig> {

	@Select("select * from sms_type_template_config where type = #{type} and channel = #{channel}")
	SmsTypeTemplateConfig selectByTypeChannel(@Param("type") String type, @Param("channel") String channel);
}