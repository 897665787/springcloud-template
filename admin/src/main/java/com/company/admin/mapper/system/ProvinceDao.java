package com.company.admin.mapper.system;

import com.company.admin.entity.system.Province;

import java.util.List;

/**
 * 省份Dao
 * Created by xuxiaowei on 2017/11/13.
 */
public interface ProvinceDao {

    int save(Province province);

    int remove(Province province);

    int update(Province province);

    int updateStatus(Province province);

    /**
     * 更新属于指定省份的城市状态
     * @param province 参数id
     * @return 受影响行数
     */
    int updateCityStatus(Province province);

    Province get(Province province);

    List<Province> list(Province province);

    List<Province> listCombo(Province province);

    Long count(Province province);

    /**
     * 获取属于指定省份的城市数量
     * @param province 参数id
     * @return 城市数量
     */
    Long countCity(Province province);


    List<Province> tree();
}
