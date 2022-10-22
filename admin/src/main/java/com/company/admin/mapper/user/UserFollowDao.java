package com.company.admin.mapper.user;

import java.util.List;

import com.company.admin.entity.user.User;
import com.company.admin.entity.user.UserFollow;

/**
 * 用户关注Dao
 * Created by JQ棣 on 2018/11/10.
 */
public interface UserFollowDao {

	List<User> list(UserFollow userFollow);

	Long count(UserFollow userFollow);

}
