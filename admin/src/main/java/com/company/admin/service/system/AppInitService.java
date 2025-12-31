package com.company.admin.service.system;

import com.company.admin.entity.base.XSPageModel;
import com.company.admin.entity.system.AppInit;
import com.company.admin.mapper.system.AppInitDao;
import com.company.admin.util.XSUuidUtil;
import com.company.framework.globalresponse.ExceptionUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * App初始化ServiceImpl
 * Created by JQ棣 on 2017/11/13.
 */
@Service
@RequiredArgsConstructor
public class AppInitService {

    private final AppInitDao appInitDao;

    public void save(AppInit appInit) {
        AppInit criteria = new AppInit();
        criteria.setKey(appInit.getKey());
        Long count = appInitDao.count(criteria);
        if (count.compareTo(0L) > 0) {
            ExceptionUtil.throwException("App初始化已存在");
        }
        appInit.setId(XSUuidUtil.generate());
        appInitDao.save(appInit);
    }

    public void remove(AppInit appInit) {
        AppInit existent = get(appInit);
        appInitDao.remove(existent);
    }

    public void update(AppInit appInit) {
        AppInit existent = get(appInit);
        if (appInit.getKey() != null) {
            AppInit criteria = new AppInit();
            criteria.setKey(appInit.getKey());
            List<AppInit> existents = appInitDao.list(criteria);
            if (existents.size() > 0) {
                boolean isSelf = existents.get(0).getId().equals(existent.getId());
                if (!isSelf) {
                    ExceptionUtil.throwException("App初始化已存在");
                }
            }
        }
        appInitDao.update(appInit);
    }

    public AppInit get(AppInit appInit) {
        AppInit existent = appInitDao.get(appInit);
        if (existent == null) {
            ExceptionUtil.throwException("App初始化不存在");
        }
        return existent;
    }

    public XSPageModel<AppInit> listAndCount(AppInit appInit) {
        appInit.setDefaultSort("create_time", "DESC");
        return XSPageModel.build(appInitDao.list(appInit), appInitDao.count(appInit));
    }
}
