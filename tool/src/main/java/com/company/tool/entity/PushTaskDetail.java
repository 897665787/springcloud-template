package com.company.tool.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@TableName("push_task_detail")
@Data
@Accessors(chain = true)
public class PushTaskDetail {
	/**
	 * ID
	 */
	private Integer id;
	/**
	 * push_task.id
	 */
	private Integer taskId;
	/**
	 * 设备ID
	 */
	private String deviceid;
	/**
	 * 模板参数json
	 */
	private String templateParamJson;
	/**
	 * 计划发送时间
	 */
	private LocalDateTime planSendTime;
	/**
	 * 实际发送时间
	 */
	private LocalDateTime actualSendTime;
	/**
	 * 状态(1:待发送,2:待定时发送,11:发送MQ成功,12:MQ消费成功,21:请求成功,22:请求失败,31:取消发送)
	 */
	private Integer status;
	/**
	 * 内容
	 */
	private String content;
	/**
	 * 意图
	 */
	private String intent;

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