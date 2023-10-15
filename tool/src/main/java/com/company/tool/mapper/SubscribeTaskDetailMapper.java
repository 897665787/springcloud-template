package com.company.tool.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.company.tool.entity.SubscribeTaskDetail;
import com.company.tool.enums.SubscribeTaskDetailEnum;

public interface SubscribeTaskDetailMapper extends BaseMapper<SubscribeTaskDetail> {

	@Update("update subscribe_task_detail set status = #{toStatus.code},remark = #{remark} where id = #{id} and status = #{fromStatus.code}")
	Integer updateStatusByStatus(@Param("toStatus") SubscribeTaskDetailEnum.Status toStatus, @Param("remark") String remark,
			@Param("id") Integer id, @Param("fromStatus") SubscribeTaskDetailEnum.Status fromStatus);
	
	@Update("update subscribe_task_detail set template_code = #{templateCode} where id = #{id}")
	Integer updateTemplateCodeById(@Param("templateCode") String templateCode, @Param("id") Integer id);

	@Update("update subscribe_task_detail set content = #{content} where id = #{id}")
	Integer updateContentById(@Param("content") String content, @Param("id") Integer id);

	@Update("update subscribe_task_detail set status = #{toStatus.code},actual_send_time = now(),remark = #{remark} where id = #{id}")
	Integer updateSendSuccessStatus(@Param("toStatus") SubscribeTaskDetailEnum.Status toStatus,
			@Param("remark") String remark, @Param("id") Integer id);

	@Update("update subscribe_task_detail set status = #{toStatus.code},remark = #{remark} where id = #{id}")
	Integer updateStatusRemark(@Param("toStatus") SubscribeTaskDetailEnum.Status toStatus, @Param("remark") String remark,
			@Param("id") Integer id);

	@Update("update subscribe_task_detail set remark = #{remark} where id = #{id}")
	Integer updateRemark(@Param("remark") String remark, @Param("id") Integer id);

	@Select("select id from subscribe_task_detail where status = #{status.code} and plan_send_time < now() limit #{limit}")
	List<Integer> select4PreTimeSend(@Param("status") SubscribeTaskDetailEnum.Status status, @Param("limit") int limit);
}