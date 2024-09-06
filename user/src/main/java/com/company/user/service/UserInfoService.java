package com.company.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.company.user.entity.UserInfo;
import com.company.user.mapper.user.UserInfoMapper;

@Component
public class UserInfoService extends ServiceImpl<UserInfoMapper, UserInfo> implements IService<UserInfo> {

	@Autowired
	private UserInfoMapper userInfoMapper;

//	@Slave
	public UserInfo selectById(Integer id) {
		UserInfo userInfo = userInfoMapper.getById(id);
		System.out.println("userInfo:" + userInfo);
		UserInfo userInfo2 = userInfoMapper.selectById(id);
		System.out.println("userInfo2:" + userInfo2);
		return userInfo;
	}
}
