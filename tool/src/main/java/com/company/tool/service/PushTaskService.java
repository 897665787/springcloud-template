package com.company.tool.service;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.company.tool.entity.PushTask;
import com.company.tool.mapper.PushTaskMapper;

@Service
public class PushTaskService extends ServiceImpl<PushTaskMapper, PushTask> {
}
