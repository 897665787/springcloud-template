package com.company.codegenerate.entity;

/**
 * 代码生成业务表配置
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
 * 代码生成业务表配置
 */
@Data
@TableName("sys_gen_table")
public class GenTable extends AuditableModel<GenTable> {

	/**
	 * id
	 */
	private Integer id;

	/**
	 * 表名
	 */
	private String tableName;

	/**
	 * 表描述
	 */
	private String tableComment;

	/**
	 * 实体类名
	 */
	private String className;

	/**
	 * 生成模板类型(crud:单表, tree:树表, sub:主子表)
	 */
	private String tplCategory;

	/**
	 * 生成包路径
	 */
	private String packageName;

	/**
	 * 生成模块名
	 */
	private String moduleName;

	/**
	 * 生成业务名
	 */
	private String businessName;

	/**
	 * 生成功能名
	 */
	private String functionName;

	/**
	 * 生成功能作者
	 */
	private String functionAuthor;

	/**
	 * 生成代码方式(0:zip压缩包 1:自定义路径)
	 */
	private String genType;

	/**
	 * 生成路径
	 */
	private String genPath;

	/**
	 * 备注
	 */
	private String remark;
}
