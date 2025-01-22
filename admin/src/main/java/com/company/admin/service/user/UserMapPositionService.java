package com.company.admin.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.company.admin.entity.base.XSPageModel;
import com.company.admin.entity.user.UserMapPosition;
import com.company.admin.exception.ExceptionConsts;
import com.company.admin.mapper.user.UserMapPositionDao;

/**
 * 定位Service
 * Created by JQ棣 on 2018/11/20.
 */
@Service
public class UserMapPositionService {

    @Autowired
    private UserMapPositionDao userMapPositionDao;

    public void save(UserMapPosition userMapPosition) {
        userMapPositionDao.save(userMapPosition);
    }

    public void remove(UserMapPosition userMapPosition) {
        UserMapPosition existent = get(userMapPosition);
        userMapPositionDao.remove(existent);
    }

    public void update(UserMapPosition userMapPosition) {
        UserMapPosition existent = get(userMapPosition);
        userMapPositionDao.update(userMapPosition);
    }

    public UserMapPosition get(UserMapPosition userMapPosition) {
        UserMapPosition existent = userMapPositionDao.get(userMapPosition);
        if (existent == null) {
            throw ExceptionConsts.DATA_NOT_EXISTED;
        }
        return existent;
    }

    public XSPageModel<UserMapPosition> listAndCount(UserMapPosition userMapPosition) {
        userMapPosition.setDefaultSort("id", "DESC");
        return XSPageModel.build(userMapPositionDao.list(userMapPosition), userMapPositionDao.count(userMapPosition));
    }
}
