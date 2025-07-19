package com.company.system.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.company.system.entity.SysDictData;

public interface SysDictDataMapper extends BaseMapper<SysDictData> {
	
	@Select("select dict_value from sys_dict_data where dict_type = #{type} and dict_code = #{code}")
	String getValueByTypeCode(@Param("type") String type, @Param("code") String code);
}