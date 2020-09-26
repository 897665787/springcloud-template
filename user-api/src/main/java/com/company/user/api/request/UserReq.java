package com.company.user.api.request;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UserReq {
	private Long id;
	private String orderCode;
	private Long seq;
}
