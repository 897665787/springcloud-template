package com.company.admin.service.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.company.admin.entity.base.XSPageModel;
import com.company.admin.entity.user.User;
import com.company.admin.entity.user.UserFollow;
import com.company.admin.mapper.user.UserFollowDao;

/**
 * 用户关注Service
 * Created by JQ棣 on 2018/11/10.
 */
@Service
@RequiredArgsConstructor
public class UserFollowService {

	private final UserFollowDao userFollowDao;
	
	public XSPageModel<User> listAndCount(UserFollow userFollow) {
		userFollow.setDefaultSort("uf.create_time", "DESC");
		return XSPageModel.build(userFollowDao.list(userFollow), userFollowDao.count(userFollow));
	}
	
}
