package com.company.system.api.request;

/**
 * 岗位信息请求
 *
 * @author JQ棣
 * @email 897665787@qq.com
 * @date 2026/10/22 10:30
 * @Copyright Copyright (c) Speediance Inc. All Rights Reserved.
 **/

import lombok.Data;

/**
 * 岗位信息请求
 */
@Data
public class SysPostReq {

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
