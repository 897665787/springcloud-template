package com.company.admin.entity.interact.dto;

import com.company.admin.entity.interact.Comment;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 点赞结果
 * 
 * Created by JQ棣 on 2018/05/17.
 */
@Accessors(chain = true)
@Getter
@Setter
public class CommentResult {

	private Integer commentNum;
	private Comment comment;

}
