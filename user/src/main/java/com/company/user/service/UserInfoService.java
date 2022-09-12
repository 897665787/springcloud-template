package com.company.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.company.user.entity.UserInfo;
import com.company.user.mapper.user.UserInfoMapper;

@Component
public class UserInfoService {

	@Autowired
	private UserInfoMapper userInfoMapper;

//	@DataSource(Slave.SLAVE2)
	public UserInfo selectById(Integer id) {
		UserInfo userInfo = userInfoMapper.getById(id);
		System.out.println("userInfo:" + userInfo);
		UserInfo userInfo2 = userInfoMapper.selectById(id);
		System.out.println("userInfo2:" + userInfo2);
		return userInfo;
	}
}
