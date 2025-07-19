package com.company.system.service;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.company.system.entity.SysUserPassword;
import com.company.system.mapper.SysUserPasswordMapper;

@Service
public class SysUserPasswordService extends ServiceImpl<SysUserPasswordMapper, SysUserPassword>
		implements IService<SysUserPassword> {

	public SysUserPassword getLastBySysUserId(Integer sysUserId) {
		return baseMapper.selectLastBySysUserId(sysUserId);
	}

	public Integer incrExpireLoginTimes(Integer id) {
		return baseMapper.incrExpireLoginTimesById(id);
	}

}