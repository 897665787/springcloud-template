package com.company.tool.service;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.company.tool.entity.EmailTask;
import com.company.tool.mapper.EmailTaskMapper;

@Service
public class EmailTaskService extends ServiceImpl<EmailTaskMapper, EmailTask> {
}
