package com.company.tool.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.company.tool.entity.SmsTemplate;

public interface SmsTemplateMapper extends BaseMapper<SmsTemplate> {

	@Select("select * from sms_template where channel = #{channel} and template_code = #{templateCode}")
	SmsTemplate selectByChannelTemplateCode(@Param("channel") String channel,
			@Param("templateCode") String templateCode);
}