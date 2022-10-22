package com.company.admin.entity.interact;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.company.admin.entity.base.BaseModel;
import com.company.admin.entity.user.User;
import com.company.admin.jackson.annotation.AutoDesc;
import com.company.admin.jackson.annotation.Emoji;
import com.company.admin.jackson.annotation.XSDateTime;

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
public class Comment extends BaseModel {

	/**
	 * ID
	 */
	private Long id;

	/**
	 * 模块，1-评论，2-demo
	 */
	@AutoDesc({ "1:评论", "2:demo" })
	private Integer module;

	/**
	 * 关联ID
	 */
	private Long relativeId;

	/**
	 * 回复的评论ID
	 */
	private Long toId;

	/**
	 * 用户ID（评论者）
	 */
	private String userId;

	/**
	 * 内容
	 */
	@Emoji
	private String content;

	/**
	 * 评论时间
	 */
	@XSDateTime
	private Date commentTime;

	/**
	 * 点赞数
	 */
	private Integer praiseNum;

	/**
	 * 评论数
	 */
	private Integer commentNum;

	/**
	 * 是否隐藏，0否，1是
	 */
	@NotNull(message = "是否隐藏不能为空", groups = Hided.class)
	@AutoDesc({ "0:展示", "1:隐藏" })
	private Integer hided;

	/**
	 * 是否删除，0否，1是
	 */
	@AutoDesc({ "0:否", "1:是" })
	private Integer deleted;

	public interface Save {
	}

	public interface Update {
	}

	public interface Hided {
	}

	private List<Comment> commentList;

	private User toUser;

	private Integer praised;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
