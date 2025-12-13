package com.company.tool.mapper.market;

import java.time.LocalDateTime;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.company.tool.api.enums.BannerEnum;
import com.company.tool.entity.Banner;

public interface BannerMapper extends BaseMapper<Banner> {

	@Select("select * from banner"
			+ " where"
			+ " begin_time < #{time} and end_time > #{time}"
			+ " and status = #{status.code}"
			+ " order by priority desc,id desc")
	List<Banner> selectValidOrderby(@Param("status") BannerEnum.Status status, @Param("time") LocalDateTime time);
}
