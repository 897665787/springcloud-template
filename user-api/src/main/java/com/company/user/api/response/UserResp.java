package com.company.user.api.response;

import java.time.LocalDateTime;

import com.company.common.jackson.annotation.AutoDesc;
import com.company.user.api.enums.UserStatus;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UserResp {
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
