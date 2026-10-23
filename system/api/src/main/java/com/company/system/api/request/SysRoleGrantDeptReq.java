package com.company.system.api.request;

import java.util.Set;

import javax.validation.constraints.NotNull;

import lombok.Data;

/**
 * 角色数据权限授权请求
 *
 * @author JQ棣
 * @email 897665787@qq.com
 * @date 2026/10/22 10:30
 * @Copyright Copyright (c) Speediance Inc. All Rights Reserved.
 **/
@Data
public class SysRoleGrantDeptReq {

	/**
	 * 角色ID
	 */
	@NotNull(message = "角色id不能为空")
	private Integer roleId;

	/**
	 * 部门ID列表
	 */
	@NotNull(message = "部门id列表不能为空")
	private Set<Integer> deptIds;
}
