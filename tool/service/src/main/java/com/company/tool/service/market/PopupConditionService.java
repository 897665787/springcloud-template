package com.company.tool.service.market;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.company.tool.entity.PopupCondition;
import com.company.tool.mapper.market.PopupConditionMapper;

@Component
public class PopupConditionService extends ServiceImpl<PopupConditionMapper, PopupCondition> implements IService<PopupCondition> {
	
	public List<PopupCondition> selectByPopupIds(Collection<Integer> popupIds) {
		if (CollectionUtils.isEmpty(popupIds)) {
			return Collections.emptyList();
		}
		return baseMapper.selectByPopupIds(popupIds);
	}
}
