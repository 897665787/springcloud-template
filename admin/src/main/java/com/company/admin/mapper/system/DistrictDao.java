package com.company.admin.mapper.system;

import com.company.admin.entity.system.District;

import java.util.List;

/**
 * 区县Dao
 * Created by JQ棣 on 2018/05/30.
 */
public interface DistrictDao {

	int save(District district);

	int remove(District district);

	int update(District district);

	int updateStatus(District district);

	District get(District district);

	List<District> list(District district);

	Long count(District district);

    List<District> listCombo(District district);
}
