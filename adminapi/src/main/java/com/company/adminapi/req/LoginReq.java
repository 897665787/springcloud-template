package com.company.adminapi.req;

import javax.validation.constraints.NotBlank;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

@Data
@Accessors(chain = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LoginReq {
	@NotBlank(message = "账号不能为空")
	String account;

	@NotBlank(message = "密码不能为空")
	String password;
	
	String uuid;

	String code;
}
