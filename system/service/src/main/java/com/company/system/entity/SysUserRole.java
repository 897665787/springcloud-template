package com.company.system.entity;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

/**
 * 用户和角色关联
 * 
 * @author CodeGenerator
 * @date 2023-04-14
 */
@Data
@TableName("sys_user_role")
public class SysUserRole {

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
	@TableField(fill = FieldFill.INSERT)
	private LocalDateTime createTime;

	/**
	 * 更新时间
	 */
	@TableField(fill = FieldFill.INSERT_UPDATE)
	private LocalDateTime updateTime;

}