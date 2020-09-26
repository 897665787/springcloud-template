package com.company.user.api.response;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UserResp {
	private Long id;
	private String orderCode;
	private Long seq;
}
