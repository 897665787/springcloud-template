package com.company.admin.entity.marketing;

import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import lombok.Data;

/**
 * 营销-优惠券使用条件
 * 
 * @author CodeGenerator
 */
@Data
@TableName("mk_coupon_use_condition")
public class CouponUseCondition {

	/**
	 * ID
	 */
	@TableId(value = "id", type = IdType.AUTO)
	private Integer id;

	/**
	 * bean名称(UseCondition的实现类)
	 */
	@TableField("bean_name")
	private String beanName;

	/**
	 * 描述
	 */
	@TableField("descrpition")
	private String descrpition;

	/**
	 * 备注
	 */
	@TableField("remark")
	private String remark;

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