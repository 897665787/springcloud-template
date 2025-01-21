package com.company.adminapi.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户和角色关联
 *
 * @author CodeGenerator
 * @date 2023-04-14
 */
@Data
public class SysUserRoleExcel {

	/**
	 * id
	 */
	@ExcelProperty(value = "id")
	private Integer id;

	/**
	 * 用户ID
	 */
	@ExcelProperty(value = "用户ID")
	private Integer userId;

	/**
	 * 角色ID
	 */
	@ExcelProperty(value = "角色ID")
	private Integer roleId;

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
	 * 更新时间
	 */
	@ExcelProperty(value = "更新时间")
	private LocalDateTime updateTime;

}
