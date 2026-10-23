package com.company.system.service;

/**
 * 通知公告服务
 *
 * @author JQ棣
 * @email 897665787@qq.com
 * @date 2026/10/22 10:30
 * @Copyright Copyright (c) Speediance Inc. All Rights Reserved.
 **/

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.company.system.entity.SysNotice;
import com.company.system.mapper.SysNoticeMapper;

/**
 * 通知公告服务
 */
@Service
public class SysNoticeService extends ServiceImpl<SysNoticeMapper, SysNotice> implements IService<SysNotice> {

}
