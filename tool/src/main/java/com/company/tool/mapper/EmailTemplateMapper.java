package com.company.tool.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.company.tool.entity.EmailTemplate;

public interface EmailTemplateMapper extends BaseMapper<EmailTemplate> {

	@Select("select * from email_template where type = #{type}")
	EmailTemplate selectByType(@Param("type") String type);
}