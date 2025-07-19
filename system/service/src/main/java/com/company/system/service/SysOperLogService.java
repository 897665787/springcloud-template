package com.company.system.service;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.company.system.entity.SysOperLog;
import com.company.system.mapper.SysOperLogMapper;

@Service
public class SysOperLogService extends ServiceImpl<SysOperLogMapper, SysOperLog>
		implements IService<SysOperLog> {

}