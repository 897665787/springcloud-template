package com.company.user.entity;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@Builder
@TableName("bu_user_oauth")
public class UserOauth {
	private Integer id;
	
	@TableField("user_id")
	private Integer userId;
	
	@TableField("identity_type")
	private String identityType;
	private String identifier;
	private String certificate;

	private String remark;
	private LocalDateTime createTime;
	private LocalDateTime updateTime;
}
