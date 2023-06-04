package com.company.admin.entity.marketing;

import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.company.admin.jackson.annotation.AutoDesc;

import lombok.Data;

/**
 * 反馈
 * 
 * @author CodeGenerator
 * @date 2023-04-14
 */
@Data
@TableName("sc_feedback")
public class Feedback {

	/**
	 * id
	 */
	@TableId(value = "id", type = IdType.AUTO)
	private Long id;

	/**
	 * 姓名
	 */
	@TableField("name")
	private String name;

	/**
	 * 手机
	 */
	@TableField("mobile")
	private String mobile;

	/**
	 * 标题
	 */
	@TableField("title")
	private String title;

	/**
	 * 内容
	 */
	@TableField("content")
	private String content;

	/**
	 * 是否解决(0:否,1:是)
	 */
	@TableField("status")
	@AutoDesc({ "0:否", "1:是" })
	private Integer status;

	/**
	 * 创建时间
	 */
	@TableField("create_time")
	private Date createTime;

	/**
	 * 更新时间
	 */
	@TableField("update_time")
	private Date updateTime;

	public interface Save {
	}

	public interface Update {
	}
}