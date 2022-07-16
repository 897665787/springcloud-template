package com.company.auth.authentication.impl.tool.dto;

import java.util.List;

import com.company.user.api.enums.UserOauthEnum;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class MobileBindAuthCode {
	/**
	 * 昵称
	 */
	private String nickname;
	/**
	 * 头像
	 */
	private String headimgurl;

	/**
	 * 授权绑定
	 */
	private List<BindUserOauth> binds;

	@Data
	@Accessors(chain = true)
	public static class BindUserOauth {
		private UserOauthEnum.IdentityType identityType;
		private String identifier;
	}
}
