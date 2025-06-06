package com.company.system.api.request;

import lombok.Data;

/**
 * 用户信息
 * 
 * @author CodeGenerator
 * @date 2023-04-14
 */
@Data
public class SysUserReq {

	/**
	 * id
	 */
	private Integer id;

	/**
	 * 账号
	 */
	private String account;

	/**
	 * 昵称
	 */
	private String nickname;

	/**
	 * 用户邮箱
	 */
	private String email;

	/**
	 * 手机号码
	 */
	private String phonenumber;

	/**
	 * 用户性别(0:男,1:女,2:未知)
	 */
	private String sex;

	/**
	 * 头像地址
	 */
	private String avatar;

	/**
	 * 帐号状态(ON:正常,OFF:停用)
	 */
	private String status;

	/**
	 * 部门ID
	 */
	private Integer deptId;

	/**
	 * 用户备注
	 */
	private String userRemark;

	/**
	 * 备注
	 */
	private String remark;

}