package com.company.user.entity;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@TableName("bu_user_source")
public class UserSource {
	private Integer id;

	/**
	 * 设备ID
	 */
	private String deviceid;
	
	/**
	 * 来源
	 */
	private String source;
	
	/**
	 * 时间
	 */
	private LocalDateTime time;

	private String remark;
	private LocalDateTime createTime;
	private LocalDateTime updateTime;
}
