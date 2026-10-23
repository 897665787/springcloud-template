package com.company.system.api.response;

/**
 * 代码生成字段配置响应
 *
 * @author JQ棣
 * @email 897665787@qq.com
 * @date 2026/10/22 10:30
 * @Copyright Copyright (c) Speediance Inc. All Rights Reserved.
 **/

import lombok.Data;

/**
 * 代码生成字段配置响应
 */
@Data
public class SysGenTableColumnResp {

	/**
	 * 字段id
	 */
	private Integer id;

	/**
	 * 所属表id
	 */
	private Integer tableId;

	/**
	 * 字段名
	 */
	private String columnName;

	/**
	 * 字段描述
	 */
	private String columnComment;

	/**
	 * 字段类型
	 */
	private String columnType;

	/**
	 * 实体属性名
	 */
	private String javaField;

	/**
	 * java类型
	 */
	private String javaType;

	/**
	 * 是否主键(1:是 0:否)
	 */
	private String isPk;

	/**
	 * 是否必填(1:是 0:否)
	 */
	private String isRequired;

	/**
	 * 是否为插入字段(1:是 0:否)
	 */
	private String isInsert;

	/**
	 * 是否编辑字段(1:是 0:否)
	 */
	private String isEdit;

	/**
	 * 是否列表字段(1:是 0:否)
	 */
	private String isList;

	/**
	 * 是否查询字段(1:是 0:否)
	 */
	private String isQuery;

	/**
	 * 查询方式
	 */
	private String queryType;

	/**
	 * 显示类型
	 */
	private String htmlType;

	/**
	 * 字典类型
	 */
	private String dictType;

	/**
	 * 排序
	 */
	private Integer sort;
}
