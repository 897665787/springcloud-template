package com.company.tool.entity;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@TableName("popup_log")
public class PopupLog {
	private Integer id;

	/**
	 * 业务类型(popup:popup,user_popup:user_popup)
	 */
	private String businessType;
	
	/**
	 * 业务ID
	 */
	private Integer businessId;
	
	/**
	 * user_info.id
	 */
	private Integer userId;

	/**
	 * 设备号(小程序是openid，APP是设备ID)
	 */
	private String deviceid;

	private String remark;
	private LocalDateTime createTime;
	private LocalDateTime updateTime;
}
