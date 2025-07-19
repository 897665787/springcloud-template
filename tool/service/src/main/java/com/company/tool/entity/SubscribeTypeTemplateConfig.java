package com.company.tool.entity;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.experimental.Accessors;

@TableName("subscribe_type_template_config")
@Data
@Accessors(chain = true)
public class SubscribeTypeTemplateConfig {
	/**
	 * ID
	 */
	private Integer id;
	/**
	 * 业务类型(SubscribeEnum.Type)
	 */
	private String type;
	/**
	 * 参数索引（逗号分隔，空代表按顺序）
	 */
	private String paramIndex;
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