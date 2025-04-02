package com.company.admin.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.company.admin.entity.base.XSPageModel;
import com.company.admin.entity.user.UserContact;
import com.company.admin.exception.ExceptionConsts;
import com.company.admin.mapper.user.UserContactDao;

/**
 * 联系人Service
 * Created by JQ棣 on 2018/11/19.
 */
@Service
public class UserContactService {

    @Autowired
    private UserContactDao userContactDao;

    public void save(UserContact userContact) {
        userContactDao.save(userContact);
    }

    public void remove(UserContact userContact) {
        UserContact existent = get(userContact);
        userContactDao.remove(existent);
    }

    public void update(UserContact userContact) {
        UserContact existent = get(userContact);
        userContactDao.update(userContact);
    }

    public UserContact get(UserContact userContact) {
        UserContact existent = userContactDao.get(userContact);
        if (existent == null) {
            throw ExceptionConsts.DATA_NOT_EXISTED;
        }
        return existent;
    }

    public XSPageModel<UserContact> listAndCount(UserContact userContact) {
        userContact.setDefaultSort("id", "DESC");
        return XSPageModel.build(userContactDao.list(userContact), userContactDao.count(userContact));
    }
}
