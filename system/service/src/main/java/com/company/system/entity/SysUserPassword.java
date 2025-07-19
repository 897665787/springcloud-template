package com.company.system.entity;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

/**
 * 用户密码
 * 
 * @author CodeGenerator
 * @date 2023-04-14
 */
@Data
@TableName("sys_user_password")
public class SysUserPassword {

	/**
	 * id
	 */
	private Integer id;

	/**
	 * sys_user.id
	 */
	private Integer sysUserId;

	/**
	 * 密码
	 */
	private String password;

	/**
	 * 过期时间
	 */
	private LocalDateTime expireTime;

	/**
	 * 过期后登录次数
	 */
	private Integer expireLoginTimes;

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