package com.company.tool.entity;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.experimental.Accessors;

@TableName("email_template")
@Data
@Accessors(chain = true)
public class EmailTemplate {
	/**
	 * ID
	 */
	private Integer id;
	/**
	 * 业务类型(verifycode:验证码,market:营销活动,tips:提示信息)
	 */
	private String type;
	/**
	 * 模板标题
	 */
	private String templateTitle;
	/**
	 * 模板内容
	 */
	private String templateContent;
	
	/**
	 * 备注信息
	 */
	private String remark;
	/**
	 * 创建时间
	 */
	private LocalDateTime createTime;

	/**
	 * 更新时间
	 */
	private LocalDateTime updateTime;
}