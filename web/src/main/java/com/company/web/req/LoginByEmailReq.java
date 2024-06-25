package com.company.web.req;

import javax.validation.constraints.NotBlank;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LoginByEmailReq {
	@NotBlank(message = "邮箱不能为空") 
	String email;
	
	@NotBlank(message = "密码不能为空") 
	String password;
	
	@NotBlank(message = "验证码不能为空") 
	String code;
}
