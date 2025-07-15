package com.company.web.resp;

import com.company.framework.jackson.annotation.AutoDesc;
import com.company.user.api.enums.UserStatus;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UserResp extends com.company.user.api.response.UserResp {
	@AutoDesc(UserStatus.class) // 需要对status字段做枚举转换，继承原类覆盖原属性并增加注解
	private Integer status;
}
