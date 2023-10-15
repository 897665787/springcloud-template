package com.company.web.req;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GrantReq {
	String group;

	/**
	 * 前端小程序授权后响应的res数据，例：
	 * 
	 * <pre>
	{
	"errMsg": "requestSubscribeMessage:ok",
	"zun-LzcQyW-edafCVvzPkK4de2Rllr1fFpw2A_x0oXE": "accept"
	}
	 * </pre>
	 */
	String resJson;
}
