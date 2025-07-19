package com.company.tool.service.market;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.company.tool.api.enums.PopupEnum;
import com.company.tool.entity.Popup;
import com.company.tool.mapper.market.PopupMapper;

@Component
public class PopupService extends ServiceImpl<PopupMapper, Popup> implements IService<Popup> {

	public List<Popup> selectByPopupLog(PopupEnum.Status status, LocalDateTime now) {
		return baseMapper.selectByPopupLog(status, now);
	}
}
