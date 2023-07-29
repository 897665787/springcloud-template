package com.company.user.service;

import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.service.IService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.company.user.entity.OpenAccessAccount;
import com.company.user.mapper.user.OpenAccessAccountMapper;

@Component
public class OpenAccessAccountService extends ServiceImpl<OpenAccessAccountMapper, OpenAccessAccount>
		implements IService<OpenAccessAccount> {

	public String getAppKeyByAppid(String appid) {
		return baseMapper.getAppKeyByAppid(appid);
	}
}
