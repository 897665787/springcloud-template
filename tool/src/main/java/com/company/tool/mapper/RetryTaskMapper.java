package com.company.tool.mapper;

import java.time.LocalDateTime;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.company.tool.entity.RetryTask;
import com.company.tool.enums.RetryTaskEnum;

public interface RetryTaskMapper extends BaseMapper<RetryTask> {

	@Update("update retry_task set status = #{status.code},json_result = #{jsonResult},remark = #{remark} where id = #{id}")
	int retrySuccess(@Param("status") RetryTaskEnum.Status callSuccess,
			@Param("jsonResult") String jsonResult, @Param("remark") String remark, @Param("id") Integer id);

	@Update("update retry_task set status = #{status.code},next_dispose_time = #{nextDisposeTime},failure = failure + 1,remark = #{remark} where id = #{id}")
	int retryFail(@Param("status") RetryTaskEnum.Status callFail, @Param("nextDisposeTime") LocalDateTime nextDisposeTime,
			@Param("remark") String remark, @Param("id") Integer id);
	
	@Update("update retry_task set status = #{status.code} where id = #{id}")
	int abandonRetry(@Param("status") RetryTaskEnum.Status abandonCall, @Param("id") Integer id);

	// 当参数只有一个的时候不能直接使用枚举传参，会报类型转换错误
	@Select("select id from retry_task where next_dispose_time <= now() and failure < max_failure and status = #{status}")
	List<Integer> selectId4CallFail(@Param("status") Integer callFail);

}