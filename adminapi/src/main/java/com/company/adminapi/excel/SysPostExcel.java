package com.company.adminapi.excel;

/**
 * 岗位信息导出
 *
 * @author JQ棣
 * @email 897665787@qq.com
 * @date 2026/10/22 10:30
 * @Copyright Copyright (c) Speediance Inc. All Rights Reserved.
 **/

import java.time.LocalDateTime;

import com.alibaba.excel.annotation.ExcelProperty;
import com.company.adminapi.easyexcel.converter.SysUserConverter;

import lombok.Data;

/**
 * 岗位信息导出
 */
@Data
public class SysPostExcel {

	/**
	 * id
	 */
	@ExcelProperty(value = "id")
	private Integer id;

	/**
	 * 岗位编码
	 */
	@ExcelProperty(value = "岗位编码")
	private String postCode;

	/**
	 * 岗位名称
	 */
	@ExcelProperty(value = "岗位名称")
	private String postName;

	/**
	 * 显示顺序
	 */
	@ExcelProperty(value = "显示顺序")
	private Integer postSort;

	/**
	 * 状态(ON:正常,OFF:停用)
	 */
	@ExcelProperty(value = "状态(ON:正常,OFF:停用)")
	private String status;

	/**
	 * 备注
	 */
	@ExcelProperty(value = "备注")
	private String remark;

	/**
	 * 创建时间
	 */
	@ExcelProperty(value = "创建时间")
	private LocalDateTime createTime;

	/**
	 * 创建人
	 */
	@ExcelProperty(value = "创建人", converter = SysUserConverter.class)
	private Integer createBy;

	/**
	 * 更新时间
	 */
	@ExcelProperty(value = "更新时间")
	private LocalDateTime updateTime;

	/**
	 * 更新人
	 */
	@ExcelProperty(value = "更新人", converter = SysUserConverter.class)
	private Integer updateBy;
}
