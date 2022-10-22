package com.company.admin.mapper.user;

import com.company.admin.entity.user.UserMapPosition;
import java.util.List;

/**
 * 定位Dao
 * Created by wjc on 2018/11/20.
 */
public interface UserMapPositionDao {

	int save(UserMapPosition userMapPosition);

	int remove(UserMapPosition userMapPosition);

	int update(UserMapPosition userMapPosition);

	UserMapPosition get(UserMapPosition userMapPosition);

	List<UserMapPosition> list(UserMapPosition userMapPosition);

	Long count(UserMapPosition userMapPosition);
}
