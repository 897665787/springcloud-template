package com.company.user.api.response;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UserOauthResp {
	private Integer userId;
	private String certificate;
}