package com.company.adminapi.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 角色和菜单关联
 *
 * @author CodeGenerator
 * @date 2023-04-14
 */
@Data
public class SysRoleMenuExcel {

	/**
	 * id
	 */
	@ExcelProperty(value = "id")
	private Integer id;

	/**
	 * 角色ID
	 */
	@ExcelProperty(value = "角色ID")
	private Integer roleId;

	/**
	 * 菜单ID
	 */
	@ExcelProperty(value = "菜单ID")
	private Integer menuId;

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
