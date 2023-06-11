package com.company.admin.entity.marketing;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.company.admin.jackson.annotation.AutoDesc;

import lombok.Data;

/**
 * 营销-用户优惠券
 * 
 * @author CodeGenerator
 */
@Data
@TableName("mk_user_coupon")
public class UserCoupon {

	/**
	 * ID
	 */
	@TableId(value = "id", type = IdType.AUTO)
	private Integer id;

	/**
	 * bu_user_info.id
	 */
	@TableField("user_id")
	private Integer userId;

	/**
	 * mk_coupon_template.id
	 */
	@TableField("coupon_template_id")
	private Integer couponTemplateId;

	/**
	 * 优惠券名称
	 */
	@TableField("name")
	private String name;

	/**
	 * 最大优惠金额
	 */
	@TableField("max_amount")
	private BigDecimal maxAmount;

	/**
	 * 折扣
	 */
	@TableField("discount")
	private BigDecimal discount;

	/**
	 * 满X金额可用
	 */
	@TableField("condition_amount")
	private BigDecimal conditionAmount;

	/**
	 * 有效期开始时间
	 */
	@TableField("begin_time")
	private Date beginTime;

	/**
	 * 有效期结束时间
	 */
	@TableField("end_time")
	private Date endTime;

	/**
	 * 状态(nouse:未使用/已使用/已过期/未激活/已失效)
	 */
	@TableField("status")
	@AutoDesc({ "nouse:未使用/已使用/已过期/未激活/已失效" })
	private String status;

	/**
	 * 最大叠加数
	 */
	@TableField("max_num")
	private Integer maxNum;

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