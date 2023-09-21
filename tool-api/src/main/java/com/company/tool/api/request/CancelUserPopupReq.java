package com.company.tool.api.request;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class CancelUserPopupReq {
	
	/**
	 * 用户ID
	 */
	@NotNull
	Integer userId;
	
	/**
	 * 标题
	 */
	@NotNull
	String title;
	
	/**
	 * 备注
	 */
	String remark;
}
