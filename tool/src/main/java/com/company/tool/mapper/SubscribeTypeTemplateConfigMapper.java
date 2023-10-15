package com.company.tool.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.company.tool.entity.SubscribeTypeTemplateConfig;

public interface SubscribeTypeTemplateConfigMapper extends BaseMapper<SubscribeTypeTemplateConfig> {

	@Select("select * from subscribe_type_template_config where type = #{type}")
	SubscribeTypeTemplateConfig selectTemplateCodeByType(@Param("type") String type);

	@Select("select type from subscribe_type_template_config where template_code = #{templateCode}")
	List<String> selectTypeByTemplateCode(@Param("templateCode") String templateCode);
}