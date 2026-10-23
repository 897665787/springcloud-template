package com.company.system.entity;

/**
 * 岗位信息
 *
 * @author JQ棣
 * @email 897665787@qq.com
 * @date 2026/10/22 10:30
 * @Copyright Copyright (c) Speediance Inc. All Rights Reserved.
 **/

import com.baomidou.mybatisplus.annotation.TableName;
import com.company.datasource.mybatisplus.activerecord.AuditableModel;

import lombok.Data;

/**
 * 岗位信息
 */
@Data
@TableName("sys_post")
public class SysPost extends AuditableModel<SysPost> {

	/**
	 * id
	 */
	private Integer id;

	/**
	 * 岗位编码
	 */
	private String postCode;

	/**
	 * 岗位名称
	 */
	private String postName;

	/**
	 * 显示顺序
	 */
	private Integer postSort;

	/**
	 * 状态(ON:正常,OFF:停用)
	 */
	private String status;

	/**
	 * 备注
	 */
	private String remark;
}
