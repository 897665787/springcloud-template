package com.company.tool.entity;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.experimental.Accessors;

@TableName("sms_type_template_config")
@Data
@Accessors(chain = true)
public class SmsTypeTemplateConfig {
	/**
	 * ID
	 */
	private Integer id;
	/**
	 * 业务类型(verifycode:验证码,market:营销活动,tips:提示信息)
	 */
	private String type;
	/**
	 * 发送渠道(log:打印日志（测试）,ali:阿里云,tencent:腾讯云)
	 */
	private String channel;
	/**
	 * 模板编码
	 */
	private String templateCode;
	
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