package com.company.auth.authentication.impl.tool.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class MpAccessToken {
	private Integer errcode;

	private String errmsg;

	@JsonProperty("access_token")
	private String accessToken;

	private String openid;

	/**
	 * <pre>
	 * https://mp.weixin.qq.com/cgi-bin/announce?action=getannouncement&announce_id=11513156443eZYea&version=&lang=zh_CN.
	 * 本接口在scope参数为snsapi_base时不再提供unionID字段。
	 * </pre>
	 */
	private String unionid;
}
