package com.company.admin.service.user;

import org.springframework.beans.factory.annotation.Autowired;
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
public class UserFollowService {

	@Autowired
	private UserFollowDao userFollowDao;
	
	public XSPageModel<User> listAndCount(UserFollow userFollow) {
		userFollow.setDefaultSort("uf.create_time", "DESC");
		return XSPageModel.build(userFollowDao.list(userFollow), userFollowDao.count(userFollow));
	}
	
}
