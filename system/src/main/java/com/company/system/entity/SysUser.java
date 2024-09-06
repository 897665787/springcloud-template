package com.company.system.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.github.encrypt.annotation.FieldEncrypt;
import io.github.encrypt.bean.Encrypted;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户信息
 * 
 * @author CodeGenerator
 * @date 2023-04-14
 */
@Data
@TableName("sys_user")
public class SysUser implements Encrypted {

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
	@FieldEncrypt
	private String email;

	/**
	 * 手机号码
	 */
	@FieldEncrypt
	private String phonenumber;

	/**
	 * 用户性别(0男 1女 2未知)
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

	/**
	 * 标记删除
	 */
	private String delFlag;

}