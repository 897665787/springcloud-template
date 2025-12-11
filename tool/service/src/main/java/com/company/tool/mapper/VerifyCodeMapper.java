package com.company.tool.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.company.tool.entity.VerifyCode;
import com.company.tool.enums.VerifyCodeEnum;

public interface VerifyCodeMapper extends BaseMapper<VerifyCode> {

	@Select("select * from verify_code where certificate = #{certificate} and type = #{type} order by id desc limit 1")
	VerifyCode selectLastByCertificateType(@Param("certificate") String certificate, @Param("type") String type);

	@Update("update verify_code set err_count = err_count + 1 where id = #{id}")
	Integer incrErrCount(@Param("id") Integer id);

	@Update("update verify_code set status = ${status.code} where id = #{id}")
	Integer updateStatus(@Param("id") Integer id, @Param("status") VerifyCodeEnum.Status status);

}