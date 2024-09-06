package com.company.tool.entity;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.experimental.Accessors;

@TableName("subscribe_task_detail")
@Data
@Accessors(chain = true)
public class SubscribeTaskDetail {
	/**
	 * ID
	 */
	private Integer id;
	/**
	 * subscribe_task.id
	 */
	private Integer taskId;
	/**
	 * 手机号码
	 */
	private String openid;
	/**
	 * 跳转页面
	 */
	private String page;
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
	 * 模板编码
	 */
	private String templateCode;
	/**
	 * 订阅消息内容
	 */
	private String content;

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