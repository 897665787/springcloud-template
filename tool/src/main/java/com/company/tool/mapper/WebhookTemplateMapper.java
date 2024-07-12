package com.company.tool.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.company.tool.entity.WebhookTemplate;

public interface WebhookTemplateMapper extends BaseMapper<WebhookTemplate> {

	@Select("select * from webhook_template where type = #{type}")
	WebhookTemplate selectByType(@Param("type") String type);
}