package com.company.system.api.request;

import java.util.Set;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class SysRoleGrantMenuReq {

    @NotNull(message = "角色id不能为空")
    private Integer roleId;

    @NotNull(message = "菜单id列表不能为空")
    private Set<Integer> menuIds;

}
