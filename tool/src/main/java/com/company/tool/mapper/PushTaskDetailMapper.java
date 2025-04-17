package com.company.tool.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.company.tool.entity.PushTaskDetail;
import com.company.tool.enums.PushTaskDetailEnum;

public interface PushTaskDetailMapper extends BaseMapper<PushTaskDetail> {

	@Update("update push_task_detail set status = #{toStatus.code},remark = #{remark} where id = #{id} and status = #{fromStatus.code}")
	Integer updateStatusByStatus(@Param("toStatus") PushTaskDetailEnum.Status toStatus, @Param("remark") String remark,
			@Param("id") Integer id, @Param("fromStatus") PushTaskDetailEnum.Status fromStatus);

	@Update("update push_task_detail set content = #{content} where id = #{id}")
	Integer updateContentById(@Param("content") String content, @Param("id") Integer id);

	@Update("update push_task_detail set status = #{toStatus.code},actual_send_time = now(),remark = #{remark} where id = #{id}")
	Integer updateSendSuccessStatus(@Param("toStatus") PushTaskDetailEnum.Status toStatus,
			@Param("remark") String remark, @Param("id") Integer id);

	@Update("update push_task_detail set status = #{toStatus.code},remark = #{remark} where id = #{id}")
	Integer updateStatusRemark(@Param("toStatus") PushTaskDetailEnum.Status toStatus, @Param("remark") String remark,
			@Param("id") Integer id);

	@Update("update push_task_detail set remark = #{remark} where id = #{id}")
	Integer updateRemark(@Param("remark") String remark, @Param("id") Integer id);

	@Select("select id from push_task_detail where status = #{status.code} and plan_send_time < now() limit #{limit}")
	List<Integer> select4PreTimeSend(@Param("status") PushTaskDetailEnum.Status status, @Param("limit") int limit);
}