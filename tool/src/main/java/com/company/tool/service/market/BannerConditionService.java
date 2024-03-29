package com.company.tool.service.market;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.service.IService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.company.tool.entity.BannerCondition;
import com.company.tool.mapper.market.BannerConditionMapper;

@Component
public class BannerConditionService extends ServiceImpl<BannerConditionMapper, BannerCondition> implements IService<BannerCondition> {
	
	public List<BannerCondition> selectByBannerIds(Collection<Integer> bannerIds) {
		if (CollectionUtils.isEmpty(bannerIds)) {
			return Collections.emptyList();
		}
		return baseMapper.selectByBannerIds(bannerIds);
	}
}
