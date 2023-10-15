package com.company.tool.entity;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotations.TableName;

import lombok.Data;
import lombok.experimental.Accessors;

@TableName("subscribe_template")
@Data
@Accessors(chain = true)
public class SubscribeTemplate {
	/**
	 * ID
	 */
	private Integer id;
	/**
	 * 添加至账号下的模板 id，发送小程序订阅消息时所需
	 */
	private String priTmplId;
	/**
	 * 标题
	 */
	private String title;
	/**
	 * 模板内容
	 */
	private String content;
	/**
	 * 模板内容示例
	 */
	private String example;
	/**
	 * 模版类型，2 为一次性订阅，3 为长期订阅
	 */
	private Integer type;
	
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