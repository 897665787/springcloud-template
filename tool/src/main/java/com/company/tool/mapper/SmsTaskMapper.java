package com.company.tool.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.company.tool.entity.SmsTask;
import com.company.tool.enums.SmsTaskEnum;

public interface SmsTaskMapper extends BaseMapper<SmsTask> {

	@Update("update sms_task set status = #{toStatus.code} where id = #{id} and status = #{fromStatus.code}")
	Integer updateStatusByStatus(@Param("toStatus") SmsTaskEnum.Status toStatus, @Param("id") Integer id,
			@Param("fromStatus") SmsTaskEnum.Status fromStatus);

	@Update("update sms_task set status = #{toStatus.code},actual_send_time = now() where id = #{id}")
	Integer updateSendSuccessStatus(@Param("toStatus") SmsTaskEnum.Status toStatus, @Param("id") Integer id);

	@Update("update sms_task set status = #{toStatus.code},remark = #{remark} where id = #{id}")
	Integer updateStatusRemark(@Param("toStatus") SmsTaskEnum.Status toStatus, @Param("remark") String remark,
			@Param("id") Integer id);

	@Update("update sms_task set remark = #{remark} where id = #{id}")
	Integer updateRemark(@Param("remark") String remark, @Param("id") Integer id);

	@Select("select id from sms_task where status = #{status.code}")
	List<Integer> select4PreTimeSend(@Param("status") SmsTaskEnum.Status status);
}