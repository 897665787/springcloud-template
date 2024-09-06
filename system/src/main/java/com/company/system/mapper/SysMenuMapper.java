package com.company.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.company.system.entity.SysMenu;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Set;

public interface SysMenuMapper extends BaseMapper<SysMenu> {

	Set<String> listPermsByMenuIds(@Param("menuIds") Set<Integer> menuIds);

    List<SysMenu> selectByUserId(@Param("userId") Integer userId);

    @Select("select * from sys_menu where status = 'ON' order by parent_id, order_num")
    List<SysMenu> selectAllOn();
}