package com.company.admin.entity.interact;

import com.company.admin.entity.base.BaseModel;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 评论数
 * Created by JQ棣 on 2018/11/09.
 */
@Accessors(chain = true)
@Setter
@Getter
public class CommentNum extends BaseModel {

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
