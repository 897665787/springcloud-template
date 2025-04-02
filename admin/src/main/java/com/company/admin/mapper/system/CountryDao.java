package com.company.admin.mapper.system;

import com.company.admin.entity.system.Country;

import java.util.List;

/**
 * 国家Dao
 * Created by JQ棣 on 2018/5/3.
 */
public interface CountryDao {

    int save(Country country);

    int remove(Country country);

    int update(Country country);

    int updateStatus(Country country);

    Country get(Country country);

    List<Country> list(Country country);

    List<Country> listCombo(Country country);

    Long count(Country country);

}
