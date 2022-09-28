package com.company.auth.resp;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

@Data
@Accessors(chain = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LoginResp {
	/** 需要绑定账号 */
	Boolean needBind;
	
	/** 用户ID（不应该返回给前端） */
//	Integer userId;

	/** token值（needBind=false） */
	String token;
}
