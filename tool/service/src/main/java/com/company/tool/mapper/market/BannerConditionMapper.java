package com.company.tool.mapper.market;

import java.util.Collection;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.company.tool.entity.BannerCondition;

public interface BannerConditionMapper extends BaseMapper<BannerCondition> {
	List<BannerCondition> selectByBannerIds(@Param("bannerIds") Collection<Integer> bannerIds);
}
