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
 * 优惠券模板
 * 
 * @author CodeGenerator
 */
@Data
@TableName("mk_coupon_template")
public class CouponTemplate {

	/**
	 * ID
	 */
	@TableId(value = "id", type = IdType.AUTO)
	private Integer id;

	/**
	 * 优惠券名称
	 */
	@TableField("name")
	private String name;

	/**
	 * 最大优惠金额(元)
	 */
	@TableField("max_amount")
	private BigDecimal maxAmount;

	/**
	 * 折扣
	 */
	@TableField("discount")
	private BigDecimal discount;

	/**
	 * 满X元可用
	 */
	@TableField("condition_amount")
	private BigDecimal conditionAmount;

	/**
	 * 发放开始时间
	 */
	@TableField("begin_time")
	private Date beginTime;

	/**
	 * 发放结束时间
	 */
	@TableField("end_time")
	private Date endTime;

	/**
	 * 有效期计算方式(fix:领券起固定有效期,ps:固定开始时间-开始时间,fx:指定行为激活后X天有效)
	 */
	@TableField("period_type")
	@AutoDesc({ "fix:领券起固定有效期","ps:固定开始时间-开始时间","fx:指定行为激活后X天有效" })
	private String periodType;

	/**
	 * 发放量
	 */
	@TableField("total_num")
	private Integer totalNum;

	/**
	 * 剩余量
	 */
	@TableField("left_num")
	private Integer leftNum;

	/**
	 * 每用户限领量
	 */
	@TableField("limit_num")
	private Integer limitNum;

	/**
	 * 使用说明
	 */
	@TableField("use_description")
	private String useDescription;

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