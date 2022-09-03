package com.company.user.entity;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotations.TableName;
import com.company.common.jackson.annotation.AutoDesc;
import com.company.user.api.enums.UserStatus;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@Builder
@TableName("sys_user")
public class User {
	private Long id;
	private String name;
	private String username;
	private String password;
	private Integer deleted;
	private String avatar;
	
	@AutoDesc(UserStatus.class)
	private Integer status;
	private String creator;
	private LocalDateTime createTime;
	private String updater;
	private LocalDateTime updateTime;
}
