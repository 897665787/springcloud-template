package com.company.order.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.company.order.entity.AliActivityNotify;

public interface AliActivityNotifyMapper extends BaseMapper<AliActivityNotify> {

	@Update("update bu_ali_activity_notify set remark = #{remark} where id = #{id}")
	int updateRemarkById(@Param("remark") String remark, @Param("id") Integer id);

}