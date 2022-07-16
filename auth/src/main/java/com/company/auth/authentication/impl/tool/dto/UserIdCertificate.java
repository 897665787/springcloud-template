package com.company.auth.authentication.impl.tool.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UserIdCertificate {
	private Integer userId;
	private String certificate;
}
