package com.company.tool.service;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.company.tool.entity.SmsTask;
import com.company.tool.mapper.SmsTaskMapper;

@Service
public class SmsTaskService extends ServiceImpl<SmsTaskMapper, SmsTask> {
}
