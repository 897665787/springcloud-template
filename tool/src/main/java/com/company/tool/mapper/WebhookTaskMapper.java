package com.company.tool.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.company.tool.entity.WebhookTask;
import com.company.tool.enums.WebhookTaskEnum;

public interface WebhookTaskMapper extends BaseMapper<WebhookTask> {

	@Update("update webhook_task set status = #{toStatus.code},remark = #{remark} where id = #{id} and status = #{fromStatus.code}")
	Integer updateStatusByStatus(@Param("toStatus") WebhookTaskEnum.Status toStatus, @Param("remark") String remark,
			@Param("id") Integer id, @Param("fromStatus") WebhookTaskEnum.Status fromStatus);

	@Update("update webhook_task set `key` = #{key},content = #{content},mentioned_list = #{mentionedList},mentioned_mobile_list = #{mentionedMobileList} where id = #{id}")
	Integer updateKeyContentMentionById(@Param("key") String key, @Param("content") String content,
			@Param("mentionedList") String mentionedList, @Param("mentionedMobileList") String mentionedMobileList,
			@Param("id") Integer id);

	@Update("update webhook_task set status = #{toStatus.code},actual_send_time = now(),remark = #{remark} where id = #{id}")
	Integer updateSendSuccessStatus(@Param("toStatus") WebhookTaskEnum.Status toStatus,
			@Param("remark") String remark, @Param("id") Integer id);

	@Update("update webhook_task set status = #{toStatus.code},remark = #{remark} where id = #{id}")
	Integer updateStatusRemark(@Param("toStatus") WebhookTaskEnum.Status toStatus, @Param("remark") String remark,
			@Param("id") Integer id);

	@Update("update webhook_task set remark = #{remark} where id = #{id}")
	Integer updateRemark(@Param("remark") String remark, @Param("id") Integer id);

	@Select("select id from webhook_task where status = #{status.code} and plan_send_time < now() limit #{limit}")
	List<Integer> select4PreTimeSend(@Param("status") WebhookTaskEnum.Status status, @Param("limit") int limit);
}