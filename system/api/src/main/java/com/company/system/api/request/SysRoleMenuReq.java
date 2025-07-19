package com.company.system.api.request;

import java.time.LocalDateTime;

import lombok.Data;

/**
 * 角色和菜单关联
 * 
 * @author CodeGenerator
 * @date 2023-04-14
 */
@Data
public class SysRoleMenuReq {

	/**
	 * id
	 */
	private Integer id;

	/**
	 * 角色ID
	 */
	private Integer roleId;

	/**
	 * 菜单ID
	 */
	private Integer menuId;

	/**
	 * 备注
	 */
	private String remark;

	/**
	 * 创建时间
	 */
	private LocalDateTime createTime;

	/**
	 * 更新时间
	 */
	private LocalDateTime updateTime;

}