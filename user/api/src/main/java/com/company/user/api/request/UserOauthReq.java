package com.company.user.api.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.company.user.api.enums.UserOauthEnum;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UserOauthReq {
	
	@NotNull(message = "用户ID不能为空")
	private Integer userId;
	
	@NotNull(message = "账号类型不能为空")
	private UserOauthEnum.IdentityType identityType;
	
	@NotBlank(message = "标识不能为空")
	private String identifier;
	
	@NotBlank(message = "凭证不能为空")
	private String certificate;
}
