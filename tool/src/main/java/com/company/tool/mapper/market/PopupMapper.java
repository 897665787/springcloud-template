package com.company.tool.mapper.market;

import java.time.LocalDateTime;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.company.tool.api.enums.PopupEnum;
import com.company.tool.entity.Popup;

public interface PopupMapper extends BaseMapper<Popup> {

	@Select("select * from mk_popup"
			+ " where"
			+ " begin_time < #{time} and end_time > #{time}"
			+ " and status = #{status.code}"
			+ " order by priority desc,id desc")
	List<Popup> selectByPopupLog(@Param("status") PopupEnum.Status status, @Param("time") LocalDateTime time);
}
