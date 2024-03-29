package com.company.tool.service.market;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.service.IService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.company.tool.api.enums.BannerEnum;
import com.company.tool.entity.Banner;
import com.company.tool.mapper.market.BannerMapper;

@Component
public class BannerService extends ServiceImpl<BannerMapper, Banner> implements IService<Banner> {

	public List<Banner> selectValidOrderby(BannerEnum.Status status, LocalDateTime now) {
		return baseMapper.selectValidOrderby(status, now);
	}
}
