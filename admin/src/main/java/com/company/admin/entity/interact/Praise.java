package com.company.admin.entity.interact;

import java.util.Date;

import javax.validation.constraints.NotNull;

import com.company.admin.entity.base.BaseModel;
import com.company.admin.jackson.annotation.AutoDesc;
import com.company.admin.jackson.annotation.XSDateTime;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 
 * Created by JQ棣 on 2018/05/17.
 */
@Accessors(chain = true)
@Setter
@Getter
public class Praise extends BaseModel {

	/**
	 * ID
	 */
	private Long id;

	/**
	 * 模块，1-评论，2-demo
	 */
	@NotNull(message = "模块不能为空", groups = Save.class)
	@AutoDesc({ "1:评论", "2:demo" })
	private Integer module;

	/**
	 * 关联ID
	 */
	@NotNull(message = "关联ID不能为空", groups = Save.class)
	private Long relativeId;

	/**
	 * 用户ID（点赞者）
	 */
	private String userId;

	/**
	 * 点赞时间
	 */
	@XSDateTime
	private Date praiseTime;
	
	public interface Save {}

}

