package com.company.tool.entity;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotations.TableName;

import lombok.Data;
import lombok.experimental.Accessors;

@TableName("sms_template")
@Data
@Accessors(chain = true)
public class SmsTemplate {
	/**
	 * ID
	 */
	private Integer id;
	/**
	 * 发送渠道(log:打印日志（测试）,ali:阿里云,tencent:腾讯云)
	 */
	private String channel;
	/**
	 * 模板编码
	 */
	private String templateCode;
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