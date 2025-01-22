package com.company.admin.service.system;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.company.admin.entity.base.XSPageModel;
import com.company.admin.entity.system.City;
import com.company.admin.entity.system.Province;
import com.company.admin.mapper.system.CityDao;
import com.company.common.exception.BusinessException;

/**
 * 城市ServiceImpl
 * Created by JQ棣 on 2017/11/13.
 */
@Service
public class CityService {

    @Autowired
    private CityDao cityDao;

    public void save(City city) {
        City criteria = new City();
        criteria.setId(city.getId());
        Long count = cityDao.count(criteria);
        if (count.compareTo(0L) > 0) {
            throw new BusinessException("城市已存在");
        }
        criteria = new City();
        criteria.setName(city.getName());
        criteria.setProvince(city.getProvince());
        count = cityDao.count(criteria);
        if (count.compareTo(0L) > 0) {
            throw new BusinessException("城市已存在");
        }
        city.setStatus(0);
        cityDao.save(city);
    }

    public void remove(City city) {
        City existent = get(city);
        cityDao.remove(existent);
    }

    public void update(City city) {
        City existent = get(city);
        City criteria = new City();
        criteria.setName(city.getName());
        criteria.setProvince(city.getProvince());
        List<City> existents = cityDao.list(criteria);
        if (existents.size() > 0) {
            boolean isSelf = existents.get(0).getId().equals(existent.getId());
            if (!isSelf) {
                throw new BusinessException("城市已存在");
            }
        }
        cityDao.update(city);
    }

    public void updateStatus(City city) {
        cityDao.updateStatus(city);
    }

    public void updateDistrictStatus(City city) {
        City existent = get(city);
        cityDao.updateDistrictStatus(existent);
    }

    public City get(City city) {
        City existent = cityDao.get(city);
        if (existent == null) {
            throw new BusinessException("城市不存在");
        }
        return existent;
    }

    public XSPageModel<City> listAndCount(City city) {
        city.setDefaultSort(new String[]{"seq", "id"}, new String[]{"DESC", "ASC"});
        return XSPageModel.build(cityDao.list(city), cityDao.count(city));
    }

    public List<City> listCombo(City city) {
        city.setDefaultSort(new String[]{"seq", "id"}, new String[]{"DESC", "ASC"});
        return cityDao.listCombo(city);
    }

    public List<Long> listSubTree(Long id) {
        if (id.toString().length() == 4) {
            return Arrays.asList(id);
        }

        City c = new City();
        c.setProvince(new Province(id));
        List<City> cities = cityDao.listCombo(c);
        List<Long> cityList = cities.stream().map(a -> a.getId()).collect(Collectors.toList());
        return cityList;
    }

    public List<Province> tree() {
        return cityDao.tree();
    }
}
