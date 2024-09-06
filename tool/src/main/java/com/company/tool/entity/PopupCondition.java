package com.company.tool.entity;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@TableName("mk_popup_condition")
public class PopupCondition {
	private Integer id;

	/**
	 * mk_popup.id
	 */
	private Integer popupId;

	/**
	 * 弹窗条件(bean名称,mk_pop_condition.bean_name)
	 */
	private String popCondition;
	/**
	 * 弹窗条件值
	 */
	private String popConditionValue;

	private String remark;
	private LocalDateTime createTime;
	private LocalDateTime updateTime;
}
