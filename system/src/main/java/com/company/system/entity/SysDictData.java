package com.company.system.entity;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

/**
 * 字典数据
 * 
 * @author CodeGenerator
 * @date 2023-04-14
 */
@Data
@TableName("sys_dict_data")
public class SysDictData {

	/**
	 * id
	 */
	private Integer id;

	/**
	 * 字典类型
	 */
	private String dictType;

	/**
	 * 字典编码
	 */
	private String dictCode;

	/**
	 * 字典值
	 */
	private String dictValue;

	/**
	 * 字典排序
	 */
	private Integer dictSort;

	/**
	 * 是否默认(Y:是,N:否)
	 */
	private String isDefault;

	/**
	 * 状态(ON:正常,OFF:停用)
	 */
	private String status;

	/**
	 * 字典备注
	 */
	private String dictRemark;

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