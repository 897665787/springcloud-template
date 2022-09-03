package com.company.order.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.company.order.entity.PayNotify;

public interface PayNotifyMapper extends BaseMapper<PayNotify> {

	@Update("update bu_pay_notify set remark = #{remark} where id = #{id}")
	int updateRemarkById(@Param("remark") String remark, @Param("id") Integer id);

}