package com.company.user.api.response;

import java.time.LocalDateTime;

import com.company.common.jackson.annotation.AutoDesc;

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
	
	@AutoDesc({ "1:正常", "2:冻结" })
	private Integer status;
	private String creator;
	private LocalDateTime createTime;
	private String updater;
	private LocalDateTime updateTime;
}
