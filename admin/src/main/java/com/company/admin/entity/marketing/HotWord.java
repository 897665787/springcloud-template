package com.company.admin.entity.marketing;

import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.company.admin.jackson.annotation.AutoDesc;

import lombok.Data;

/**
 * 热搜词
 * 
 * @author CodeGenerator
 * @date 2023-04-14
 */
@Data
@TableName("sc_hot_word")
public class HotWord {

	/**
	 * 编号
	 */
	@TableId(value = "id", type = IdType.AUTO)
	private Long id;

	/**
	 * 名称
	 */
	@TableField("name")
	private String name;

	/**
	 * 状态(0:关闭,1:启用)
	 */
	@TableField("status")
	@AutoDesc({ "0:关闭", "1:启用" })
	private Integer status;

	/**
	 * 顺序
	 */
	@TableField("seq")
	private Integer seq;

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