package com.company.admin.entity.interact;

import com.company.admin.entity.base.BaseModel;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 点赞数
 * Created by JQ棣 on 2018/11/08.
 */
@Accessors(chain = true)
@Setter
@Getter
public class PraiseNum extends BaseModel {

	/**
	 * 模块，1-评论，2-demo
	 */
	private Integer module;

	/**
	 * 关联ID
	 */
	private Long relativeId;

	/**
	 * 数量
	 */
	private Integer number;
	
}
