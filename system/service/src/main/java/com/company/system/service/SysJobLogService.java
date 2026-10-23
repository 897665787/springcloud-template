package com.company.system.service;

/**
 * 定时任务日志服务
 *
 * @author JQ棣
 * @email 897665787@qq.com
 * @date 2026/10/22 10:30
 * @Copyright Copyright (c) Speediance Inc. All Rights Reserved.
 **/

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.company.system.entity.SysJobLog;
import com.company.system.mapper.SysJobLogMapper;

/**
 * 定时任务日志服务
 */
@Service
public class SysJobLogService extends ServiceImpl<SysJobLogMapper, SysJobLog> implements IService<SysJobLog> {

}
