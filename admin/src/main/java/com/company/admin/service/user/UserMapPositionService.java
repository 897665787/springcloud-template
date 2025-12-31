package com.company.admin.service.user;

import com.company.admin.entity.base.XSPageModel;
import com.company.admin.entity.user.UserMapPosition;
import com.company.admin.mapper.user.UserMapPositionDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 定位Service
 * Created by JQ棣 on 2018/11/20.
 */
@Service
@RequiredArgsConstructor
public class UserMapPositionService {

    private final UserMapPositionDao userMapPositionDao;

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
        return existent;
    }

    public XSPageModel<UserMapPosition> listAndCount(UserMapPosition userMapPosition) {
        userMapPosition.setDefaultSort("id", "DESC");
        return XSPageModel.build(userMapPositionDao.list(userMapPosition), userMapPositionDao.count(userMapPosition));
    }
}
