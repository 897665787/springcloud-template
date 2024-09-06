package com.company.system.api.request;

import java.util.Set;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class SysUserAssignRoleReq {

    @NotNull(message = "用户id不能为空")
    private Integer sysUserId;

    @NotNull(message = "角色列表不能为null")
    private Set<Integer> sysRoleIds;

}
