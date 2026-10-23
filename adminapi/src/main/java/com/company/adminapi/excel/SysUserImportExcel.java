package com.company.adminapi.excel;

/**
 * 用户导入模板
 *
 * @author JQ棣
 * @email 897665787@qq.com
 * @date 2026/10/22 10:30
 * @Copyright Copyright (c) Speediance Inc. All Rights Reserved.
 **/

import com.alibaba.excel.annotation.ExcelProperty;

import lombok.Data;

/**
 * 用户导入模板
 * <p>
 * 表头与若依导入模板保持一致，列顺序固定。
 */
@Data
public class SysUserImportExcel {

	/**
	 * 账号
	 */
	@ExcelProperty(value = "用户名称", index = 0)
	private String account;

	/**
	 * 昵称
	 */
	@ExcelProperty(value = "用户昵称", index = 1)
	private String nickname;

	/**
	 * 部门编号
	 */
	@ExcelProperty(value = "部门编号", index = 2)
	private Integer deptId;

	/**
	 * 手机号码
	 */
	@ExcelProperty(value = "手机号码", index = 3)
	private String phonenumber;

	/**
	 * 用户邮箱
	 */
	@ExcelProperty(value = "用户邮箱", index = 4)
	private String email;

	/**
	 * 用户性别(0男 1女 2未知)
	 */
	@ExcelProperty(value = "用户性别", index = 5)
	private String sex;

	/**
	 * 帐号状态(0正常 1停用)
	 */
	@ExcelProperty(value = "帐号状态", index = 6)
	private String status;
}
