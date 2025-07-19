package com.company.tool.entity;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@TableName("mk_user_popup")
public class UserPopup {
	private Integer id;

	/**
	 * bu_user_info.id
	 */
	private Integer userId;

	/**
	 * 有效期开始时间
	 */
	private LocalDateTime beginTime;

	/**
	 * 有效期结束时间
	 */
	private LocalDateTime endTime;

	/**
	 * 状态(off:下架,on:上架)
	 */
	private String status;

	/**
	 * 优先级(值越大，优先级越高)
	 */
	private Integer priority;
	
	/**
	 * 标题
	 */
	private String title;
	
	/**
	 * 文案
	 */
	private String text;
	
	/**
	 * 背景图json数据
	 */
	private String bgImg;

	/**
	 * 关闭按钮json数据
	 */
	private String closeBtn;

	private String remark;
	private LocalDateTime createTime;
	private LocalDateTime updateTime;
}
