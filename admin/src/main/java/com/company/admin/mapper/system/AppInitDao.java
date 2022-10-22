package com.company.admin.mapper.system;

import com.company.admin.entity.system.AppInit;

import java.util.List;

/**
 * App初始化Dao
 * Created by xuxiaowei on 2017/11/13.
 */
public interface AppInitDao {

    int save(AppInit appInit);

    int remove(AppInit appInit);

    int update(AppInit appInit);

    AppInit get(AppInit appInit);

    List<AppInit> list(AppInit appInit);

    Long count(AppInit appInit);
}
