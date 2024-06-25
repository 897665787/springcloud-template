package com.company.web.req;

import javax.validation.constraints.NotBlank;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RegByMobileReq {
	@NotBlank(message = "手机号不能为空") 
	String mobile;
	
	@NotBlank(message = "密码不能为空") 
	String password;
	
	@NotBlank(message = "验证码不能为空") 
	String code;
}
