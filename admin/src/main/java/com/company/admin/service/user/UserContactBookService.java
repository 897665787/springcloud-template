package com.company.admin.service.user;

import com.company.admin.entity.base.XSPageModel;
import com.company.admin.entity.user.UserContactBook;
import com.company.admin.mapper.user.UserContactBookDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 通讯录Service
 * Created by JQ棣 on 2018/11/19.
 */
@Service
public class UserContactBookService {

    @Autowired
    private UserContactBookDao userContactBookDao;

    public void save(UserContactBook userContactBook) {
        userContactBookDao.save(userContactBook);
    }

    public void remove(UserContactBook userContactBook) {
        UserContactBook existent = get(userContactBook);
        userContactBookDao.remove(existent);
    }

    public void update(UserContactBook userContactBook) {
        UserContactBook existent = get(userContactBook);
        userContactBookDao.update(userContactBook);
    }

    public UserContactBook get(UserContactBook userContactBook) {
        UserContactBook existent = userContactBookDao.get(userContactBook);
        return existent;
    }

    public XSPageModel<UserContactBook> listAndCount(UserContactBook userContactBook) {
        userContactBook.setDefaultSort("id", "DESC");
        return XSPageModel.build(userContactBookDao.list(userContactBook), userContactBookDao.count(userContactBook));
    }
}
