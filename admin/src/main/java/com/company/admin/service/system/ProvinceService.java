package com.company.admin.service.system;

import java.util.List;

import com.company.framework.globalresponse.ExceptionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.company.admin.entity.base.XSPageModel;
import com.company.admin.entity.system.Province;
import com.company.admin.mapper.system.ProvinceDao;

/**
 * 省份ServiceImpl
 * Created by JQ棣 on 2017/11/13.
 */
@Service
public class ProvinceService {

    @Autowired
    private ProvinceDao provinceDao;

    public void save(Province province) {
        Province criteria = new Province();
        criteria.setId(province.getId());
        Long count = provinceDao.count(criteria);
        if (count.compareTo(0L) > 0) {
            ExceptionUtil.throwException("省份已存在");
        }
        criteria = new Province();
        criteria.setName(province.getName());
        count = provinceDao.count(criteria);
        if (count.compareTo(0L) > 0) {
            ExceptionUtil.throwException("省份已存在");
        }
        province.setStatus(0);
        provinceDao.save(province);
    }

    public void remove(Province province) {
        Province existent = get(province);
        Long cityCount = provinceDao.countCity(existent);
        if (cityCount.compareTo(0L) > 0) {
            ExceptionUtil.throwException("省份被使用");
        }
        provinceDao.remove(existent);
    }

    public void update(Province province) {
        Province existent = get(province);
        Province criteria = new Province();
        criteria.setName(province.getName());
        List<Province> existents = provinceDao.list(criteria);
        if (existents.size() > 0) {
            boolean isSelf = existents.get(0).getId().equals(existent.getId());
            if (!isSelf) {
                ExceptionUtil.throwException("省份已存在");
            }
        }
        provinceDao.update(province);
    }

    public void updateStatus(Province province) {
        provinceDao.updateStatus(province);
    }

    public void updateCityStatus(Province province) {
        Province existent = get(province);
        provinceDao.updateCityStatus(existent);
    }

    public Province get(Province province) {
        Province existent = provinceDao.get(province);
        if (existent == null) {
            ExceptionUtil.throwException("省份不存在");
        }
        return existent;
    }

    public XSPageModel<Province> listAndCount(Province province) {
        province.setDefaultSort(new String[]{"seq", "id"}, new String[]{"DESC", "ASC"});
        return XSPageModel.build(provinceDao.list(province), provinceDao.count(province));
    }

    public List<Province> listCombo(Province province) {
        province.setDefaultSort(new String[]{"seq", "id"}, new String[]{"DESC", "ASC"});
        return provinceDao.listCombo(province);
    }


    public List<Province> tree() {
        return provinceDao.tree();
    }
}
