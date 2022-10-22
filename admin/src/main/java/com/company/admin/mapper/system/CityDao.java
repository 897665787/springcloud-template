package com.company.admin.mapper.system;


import com.company.admin.entity.system.City;
import com.company.admin.entity.system.Province;

import java.util.List;

/**
 * 城市Dao
 * Created by xuxiaowei on 2017/11/13.
 */
public interface CityDao {

    int save(City city);

    int remove(City city);

    int update(City city);

    int updateStatus(City city);

    int updateDistrictStatus(City city);

    City get(City city);

    List<City> list(City city);

    List<City> listCombo(City city);

    Long count(City city);

    List<Province> tree();
}
