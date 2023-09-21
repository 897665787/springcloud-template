package com.company.tool.mapper.market;

import java.time.LocalDateTime;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.company.tool.api.enums.PopupEnum;
import com.company.tool.entity.UserPopup;

public interface UserPopupMapper extends BaseMapper<UserPopup> {

	@Select("select p.*"
			+ " from mk_user_popup p"
			+ " left join mk_popup_log pl on pl.business_id = p.id and pl.business_type = #{businessType.code}"
			+ " where"
			+ " p.user_id = #{userId}"
			+ " and p.begin_time < #{time} and p.end_time > #{time}"
			+ " and p.status = #{status.code}"
			+ " and pl.id is null"
			+ " order by p.priority desc,p.id desc"
			+ " limit 1")
	UserPopup selectByPopupLog(@Param("userId") Integer userId,
			@Param("businessType") PopupEnum.LogBusinessType businessType, @Param("status") PopupEnum.Status status,
			@Param("time") LocalDateTime time);

	@Select("select * from mk_user_popup where user_id = #{userId} and title = #{title}")
	List<UserPopup> selectByUserIdTitle(@Param("userId") Integer userId, @Param("title") String title);
}
