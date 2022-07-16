package com.company.user.api.request;

import javax.validation.constraints.NotBlank;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UserInfoReq {
	@NotBlank(message = "手机号不能为空")
	private String mobile;
	
	private String nickname;
	private String avator;
	
}
