package com.company.system.api.request;

import lombok.Data;

@Data
public class RemindPasswordExpireReq {
	private Integer sysUserId;
	private Integer sysUserPasswordId;
}