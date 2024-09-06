package com.company.system.entity;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

/**
 * 部门
 * 
 * @author CodeGenerator
 * @date 2023-04-14
 */
@Data
@TableName("sys_dept")
public class SysDept {

	/**
	 * id
	 */
	private Integer id;

	/**
	 * 父部门id
	 */
	private Integer parentId;

	/**
	 * 父部门id列表
	 */
	private String parentIds;

	/**
	 * 部门名称
	 */
	private String name;

	/**
	 * 显示顺序
	 */
	private Integer orderNum;

	/**
	 * 部门状态(ON:正常,OFF:停用)
	 */
	private String status;

	/**
	 * 备注
	 */
	private String remark;

	/**
	 * 创建时间
	 */
	@TableField(fill = FieldFill.INSERT)
	private LocalDateTime createTime;

	/**
	 * 创建人
	 */
	@TableField(fill = FieldFill.INSERT)
	private Integer createBy;

	/**
	 * 更新时间
	 */
	@TableField(fill = FieldFill.INSERT_UPDATE)
	private LocalDateTime updateTime;

	/**
	 * 更新人
	 */
	@TableField(fill = FieldFill.INSERT_UPDATE)
	private Integer updateBy;

}