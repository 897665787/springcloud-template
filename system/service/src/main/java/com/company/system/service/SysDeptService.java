package com.company.system.service;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.company.system.entity.SysDept;
import com.company.system.mapper.SysDeptMapper;

@Service
public class SysDeptService extends ServiceImpl<SysDeptMapper, SysDept>
		implements IService<SysDept> {

}