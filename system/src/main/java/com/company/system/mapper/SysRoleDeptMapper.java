package com.company.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.company.system.entity.SysRoleDept;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Set;

public interface SysRoleDeptMapper extends BaseMapper<SysRoleDept> {

    @Select("select role_id from sys_role_dept where dept_id = #{deptId}")
    Set<Integer> listRoleIdsByDeptId(@Param("deptId") Integer deptId);

}