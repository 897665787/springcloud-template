package com.company.user.mapper.common;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.company.user.entity.City;

@DS("common")
public interface CityMapper extends BaseMapper<City> {
}
