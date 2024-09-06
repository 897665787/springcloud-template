package com.company.tool.mapper.market;

import java.util.Collection;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.company.tool.entity.PopupCondition;

public interface PopupConditionMapper extends BaseMapper<PopupCondition> {
	List<PopupCondition> selectByPopupIds(@Param("popupIds") Collection<Integer> popupIds);
}
