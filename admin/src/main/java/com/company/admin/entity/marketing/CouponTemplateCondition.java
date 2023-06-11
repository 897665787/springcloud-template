package com.company.admin.entity.marketing;

import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import lombok.Data;

/**
 * 营销-优惠券模板-使用条件
 * 
 * @author CodeGenerator
 */
@Data
@TableName("mk_coupon_template_condition")
public class CouponTemplateCondition {

	/**
	 * ID
	 */
	@TableId(value = "id", type = IdType.AUTO)
	private Integer id;

	/**
	 * mk_coupon_template.id
	 */
	@TableField("coupon_template_id")
	private Integer couponTemplateId;

	/**
	 * 使用条件(bean名称,mk_coupon_use_condition.bean_name)
	 */
	@TableField("use_condition")
	private String useCondition;

	/**
	 * 使用条件值
	 */
	@TableField("use_condition_value")
	private String useConditionValue;

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