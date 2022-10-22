package com.company.admin.service.system;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.company.admin.entity.base.XSPageModel;
import com.company.admin.entity.system.Version;
import com.company.admin.mapper.system.VersionDao;
import com.company.common.exception.BusinessException;

/**
 * 版本管理ServiceImpl
 * Created by gustinlau on 11/1/17.
 */
@Service
public class VersionService {

    @Autowired
    VersionDao versionDao;

    public void save(Version version) {
        Version existent = new Version();
        existent.setCode(version.getCode());
        existent.setPlatform(version.getPlatform());
        Long count = versionDao.count(existent);
        if (count.compareTo(0L) > 0) {
            throw new BusinessException("版本已存在");
        }
        versionDao.save(version);
    }

    public void remove(Version version) {
        Version existent = get(version);
        versionDao.remove(existent);
    }

    public void update(Version version) {
        versionDao.update(version);
    }

    public Version get(Version version) {
        Version existent = versionDao.get(version);
        if (existent == null) {
            throw new BusinessException("版本不存在");
        }
        return existent;
    }

    public XSPageModel<Version> listAndCount(Version version) {
        version.setDefaultSort("code", "DESC");
        return XSPageModel.build(versionDao.list(version), versionDao.count(version));
    }
}
