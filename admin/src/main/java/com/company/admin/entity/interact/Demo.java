package com.company.admin.entity.interact;

import com.company.admin.entity.base.BaseModel;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 
 * Created by JQ棣 on 2018/05/17.
 */
@Accessors(chain = true)
@Getter
@Setter
public class Demo extends BaseModel {

	/**
	 * ID
	 */
	private Long id;

	/**
	 * 用户ID（点赞者）
	 */
	private String userId;
	
	/**
	 * 点赞数
	 */
	private Integer praiseNum;

	/**
	 * 评论数
	 */
	private Integer commentNum;

	/**
	 * 收藏数
	 */
	private Integer collectNum;
	
	public interface Save {}
	
	public interface Update {}

}

