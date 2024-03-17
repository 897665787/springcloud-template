package com.company.order.mapper;

import java.time.LocalDateTime;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.company.order.entity.InnerCallback;
import com.company.order.enums.InnerCallbackEnum;

public interface InnerCallbackMapper extends BaseMapper<InnerCallback> {

	@Update("update bu_inner_callback set status = #{status.code},json_result = #{jsonResult},remark = #{remark} where id = #{id}")
	int callbackSuccess(@Param("status") InnerCallbackEnum.Status callbackSuccess,
			@Param("jsonResult") String jsonResult, @Param("remark") String remark, @Param("id") Integer id);

	@Update("update bu_inner_callback set status = #{status.code},next_dispose_time = #{nextDisposeTime},failure = failure + 1,remark = #{remark} where id = #{id}")
	int callbackFail(@Param("status") InnerCallbackEnum.Status callbackFail, @Param("nextDisposeTime") LocalDateTime nextDisposeTime,
			@Param("remark") String remark, @Param("id") Integer id);
	
	@Update("update bu_inner_callback set status = #{status.code} where id = #{id}")
	int abandonCallback(@Param("status") InnerCallbackEnum.Status abandonCallback, @Param("id") Integer id);

	// 当参数只有一个的时候不能直接使用枚举传参，会报类型转换错误
	@Select("select id from bu_inner_callback where next_dispose_time <= now() and failure < max_failure and status = #{status}")
	List<Integer> selectId4CallbackFail(@Param("status") Integer status);

}