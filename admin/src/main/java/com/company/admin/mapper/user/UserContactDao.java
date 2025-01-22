package com.company.admin.mapper.user;

import com.company.admin.entity.user.UserContact;
import java.util.List;

/**
 * 联系人Dao
 * Created by JQ棣 on 2018/11/19.
 */
public interface UserContactDao {

	int save(UserContact userContact);

	int remove(UserContact userContact);

	int update(UserContact userContact);

	UserContact get(UserContact userContact);

	List<UserContact> list(UserContact userContact);

	Long count(UserContact userContact);
}
