package com.company.system.api.request;

import java.time.LocalDateTime;

import lombok.Data;

/**
 * 用户和角色关联
 * 
 * @author CodeGenerator
 * @date 2023-04-14
 */
@Data
public class SysUserRoleReq {

	/**
	 * id
	 */
	private Integer id;

	/**
	 * 用户ID
	 */
	private Integer userId;

	/**
	 * 角色ID
	 */
	private Integer roleId;

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