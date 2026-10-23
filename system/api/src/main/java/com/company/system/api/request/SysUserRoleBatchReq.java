package com.company.system.api.request;

import java.util.Set;

import javax.validation.constraints.NotNull;

import lombok.Data;

/**
 * 用户和角色批量关联请求
 *
 * @author JQ棣
 * @email 897665787@qq.com
 * @date 2026/10/22 10:30
 * @Copyright Copyright (c) Speediance Inc. All Rights Reserved.
 **/
@Data
public class SysUserRoleBatchReq {

	/**
	 * 角色ID
	 */
	private Integer roleId;

	/**
	 * 角色ID列表
	 */
	private Set<Integer> roleIds;

	/**
	 * 用户ID列表
	 */
	@NotNull(message = "用户id列表不能为空")
	private Set<Integer> userIds;
}
