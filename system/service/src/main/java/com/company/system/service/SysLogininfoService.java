package com.company.system.service;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.company.system.entity.SysLogininfo;
import com.company.system.mapper.SysLogininfoMapper;

@Service
public class SysLogininfoService extends ServiceImpl<SysLogininfoMapper, SysLogininfo>
		implements IService<SysLogininfo> {

}