package com.company.system.service;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.company.system.entity.SysDictType;
import com.company.system.mapper.SysDictTypeMapper;

@Service
public class SysDictTypeService extends ServiceImpl<SysDictTypeMapper, SysDictType>
		implements IService<SysDictType> {

}