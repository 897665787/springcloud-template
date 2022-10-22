package com.company.admin.entity.user;

import com.company.admin.entity.base.BaseModel;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 关注粉丝数
 * Created by JQ棣 on 2018/11/10.
 */
@Accessors(chain = true)
@Getter
@Setter
public class UserNum extends BaseModel {

	/**
	 * 用户ID
	 */
	private String userId;

	/**
	 * 关注数
	 */
	private Long followNumber;

	/**
	 * 粉丝数
	 */
	private Long fansNumber;

}
