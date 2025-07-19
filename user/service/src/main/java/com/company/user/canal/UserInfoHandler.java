package com.company.user.canal;

import org.springframework.stereotype.Component;

import com.company.framework.util.JsonUtil;
import com.company.user.entity.UserInfo;

import top.javatool.canal.client.annotation.CanalTable;
import top.javatool.canal.client.handler.EntryHandler;

@Component
@CanalTable(value = "bu_user_info")
public class UserInfoHandler implements EntryHandler<UserInfo> {

	@Override
	public void delete(UserInfo t) {
		System.out.println("UserInfoHandler.delete()");
		System.out.println(JsonUtil.toJsonString(t));
	}

	@Override
	public void insert(UserInfo t) {
		System.out.println("UserInfoHandler.insert()");
		System.out.println(JsonUtil.toJsonString(t));
	}

	@Override
	public void update(UserInfo before, UserInfo after) {
		System.out.println("UserInfoHandler.update()");
		System.out.println("before:" + JsonUtil.toJsonString(before));
		System.out.println("after:" + JsonUtil.toJsonString(after));
	}

}
