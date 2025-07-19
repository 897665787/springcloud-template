package com.company.tool.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.company.tool.entity.SubscribeTemplate;

public interface SubscribeTemplateMapper extends BaseMapper<SubscribeTemplate> {

	@Select("select * from subscribe_template where pri_tmpl_id = #{priTmplId}")
	SubscribeTemplate selectByPriTmplId(@Param("priTmplId") String priTmplId);
}