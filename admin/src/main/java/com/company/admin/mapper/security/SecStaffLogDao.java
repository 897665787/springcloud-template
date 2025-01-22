package com.company.admin.mapper.security;

import com.company.admin.entity.security.SecStaffLog;

import java.util.List;

/**
 * 系统用户日志Dao
 * Created by JQ棣 on 2017/11/2.
 */
public interface SecStaffLogDao {

    int save(SecStaffLog secStaffLog);

    int remove(SecStaffLog secStaffLog);

    SecStaffLog get(SecStaffLog secStaffLog);

    List<SecStaffLog> list(SecStaffLog secStaffLog);

    Long count(SecStaffLog secStaffLog);
}
