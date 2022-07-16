package com.company.auth.authentication.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class LoginResult {
	
	Boolean success;
	
	String message;
	
	Integer userId;
}
