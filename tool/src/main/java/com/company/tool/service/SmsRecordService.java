package com.company.tool.service;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.company.tool.entity.SmsRecord;
import com.company.tool.mapper.SmsRecordMapper;

@Service
public class SmsRecordService extends ServiceImpl<SmsRecordMapper, SmsRecord> {
}
