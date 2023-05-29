package com.company.tool.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.company.tool.entity.VerifyCode;

public interface VerifyCodeMapper extends BaseMapper<VerifyCode> {

	@Select("select * from bu_verify_code where certificate = #{certificate} and type = #{type} order by id desc limit 1")
	VerifyCode selectLastByCertificateType(@Param("certificate") String certificate, @Param("type") String type);

}