package com.company.admin.service.system;

import java.util.List;

import com.company.framework.globalresponse.ExceptionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import com.company.admin.entity.base.XSPageModel;
import com.company.admin.entity.system.Country;
import com.company.admin.mapper.system.CountryDao;

/**
 * 国家ServiceImpl
 * Created by JQ棣 on 2018/5/3.
 */
@Service
public class CountryService {
    
    @Autowired
    private CountryDao countryDao;

    public void save(Country country) {
        try {
            countryDao.save(country);
        } catch (DuplicateKeyException e) {
            ExceptionUtil.throwException("国家已存在");
        }
    }

    public void remove(Country country) {
        Country existent = get(country);
        countryDao.remove(existent);
    }

    public void update(Country country) {
        try {
            countryDao.update(country);
        } catch (DuplicateKeyException e) {
            ExceptionUtil.throwException("国家已存在");
        }
    }

    public void updateStatus(Country country) {
        countryDao.updateStatus(country);
    }

    public Country get(Country country) {
        Country existent = countryDao.get(country);
        if (existent == null) {
            ExceptionUtil.throwException("国家不存在");
        }
        return existent;
    }

    public XSPageModel<Country> listAndCount(Country country) {
        country.setDefaultSort(new String[]{"seq", "id"}, new String[]{"DESC", "ASC"});
        return XSPageModel.build(countryDao.list(country), countryDao.count(country));
    }

    public List<Country> listCombo(Country country) {
        country.setDefaultSort("seq", "DESC");
        return countryDao.listCombo(country);
    }
}
