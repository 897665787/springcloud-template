package com.company.user.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
@TableName("user_device")
public class UserDevice {
	private Integer id;

	/**
	 * bu_user_info.id
	 */
	private Integer userId;

	/**
	 * 设备ID
	 */
	private String deviceid;

	private String remark;
	private LocalDateTime createTime;
	private LocalDateTime updateTime;
}
