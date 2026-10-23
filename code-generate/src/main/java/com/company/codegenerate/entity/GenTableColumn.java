package com.company.codegenerate.entity;

/**
 * 代码生成表字段配置
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
 * 代码生成表字段配置
 */
@Data
@TableName("sys_gen_table_column")
public class GenTableColumn extends AuditableModel<GenTableColumn> {

	/**
	 * id
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
	 * 查询方式(EQ:等于,NE:不等于,GT:大于,LT:小于,LIKE:模糊,BETWEEN:范围)
	 */
	private String queryType;

	/**
	 * 显示类型(input:文本框,textarea:文本域,select:下拉框,radio:单选框,datetime:日期控件,imageUpload:图片上传,fileUpload:文件上传,editor:富文本控件)
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
