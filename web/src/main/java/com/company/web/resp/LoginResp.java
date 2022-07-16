package com.company.web.resp;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

@Data
@Accessors(chain = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LoginResp {
	/** 用户ID */
	Integer userId;

	/** token值 */
	String token;
}
