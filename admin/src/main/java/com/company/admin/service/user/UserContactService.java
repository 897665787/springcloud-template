package com.company.admin.service.user;

import com.company.admin.entity.base.XSPageModel;
import com.company.admin.entity.user.UserContact;
import com.company.admin.mapper.user.UserContactDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 联系人Service
 * Created by JQ棣 on 2018/11/19.
 */
@Service
@RequiredArgsConstructor
public class UserContactService {

    private final UserContactDao userContactDao;

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
        return existent;
    }

    public XSPageModel<UserContact> listAndCount(UserContact userContact) {
        userContact.setDefaultSort("id", "DESC");
        return XSPageModel.build(userContactDao.list(userContact), userContactDao.count(userContact));
    }
}
