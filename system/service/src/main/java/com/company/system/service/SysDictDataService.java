package com.company.system.service;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.company.system.entity.SysDictData;
import com.company.system.mapper.SysDictDataMapper;

@Service
public class SysDictDataService extends ServiceImpl<SysDictDataMapper, SysDictData> implements IService<SysDictData> {

	public String getValueByTypeCode(String type, String code) {
		return baseMapper.getValueByTypeCode(type, code);
	}

}