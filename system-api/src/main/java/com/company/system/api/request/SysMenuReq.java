package com.company.system.api.request;

import java.time.LocalDateTime;

import lombok.Data;

/**
 * 菜单权限
 * 
 * @author CodeGenerator
 * @date 2023-04-14
 */
@Data
public class SysMenuReq {

	/**
	 * id
	 */
	private Integer id;

	/**
	 * 父菜单ID
	 */
	private Integer parentId;

	/**
	 * 菜单图标
	 */
	private String icon;

	/**
	 * 菜单名称
	 */
	private String menuName;

	/**
	 * 显示顺序
	 */
	private Integer orderNum;

	/**
	 * 路由地址
	 */
	private String path;

	/**
	 * 重定向
	 */
	private String redirect;

	/**
	 * 组件路径
	 */
	private String component;

	/**
	 * 菜单类型(M:目录,C:菜单,F:按钮)
	 */
	private String menuType;

	/**
	 * 菜单状态(ON:正常,OFF:停用)
	 */
	private String status;

	/**
	 * 菜单状态(1:显示,0:隐藏)
	 */
	private Integer visible;

	/**
	 * 权限标识
	 */
	private String perms;

	/**
	 * 备注
	 */
	private String remark;

	/**
	 * 创建时间
	 */
	private LocalDateTime createTime;

	/**
	 * 创建人
	 */
	private Integer createBy;

	/**
	 * 更新时间
	 */
	private LocalDateTime updateTime;

	/**
	 * 更新人
	 */
	private Integer updateBy;

}