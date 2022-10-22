package com.company.admin.entity.user;

import com.company.admin.entity.base.BaseModel;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 积分记录
 * Created by JQ棣 on 2018/11/12.
 */
@Accessors(chain = true)
@Getter
@Setter
public class CreditLog extends BaseModel {

	/**
	 * ID
	 */
	private Long id;

	/**
	 * 用户ID
	 */
	private String userId;

	/**
	 * 积分
	 */
	private Long credit;

	/**
	 * 备注
	 */
	private String remark;

	public interface Save {}

	public interface Update {}
	
	private Integer type;
}
