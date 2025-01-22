package com.company.admin.mapper.user;

import com.company.admin.entity.user.UserContactBook;
import java.util.List;

/**
 * 通讯录Dao
 * Created by JQ棣 on 2018/11/19.
 */
public interface UserContactBookDao {

	int save(UserContactBook userContactBook);

	int remove(UserContactBook userContactBook);

	int update(UserContactBook userContactBook);

	UserContactBook get(UserContactBook userContactBook);

	List<UserContactBook> list(UserContactBook userContactBook);

	Long count(UserContactBook userContactBook);
}
