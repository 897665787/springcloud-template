package com.company.order.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

/**
 * 订单商品
 */
@Data
@TableName("bu_order_product")
public class OrderProduct {
	/**
	 * ID
	 */
	@TableId(value = "id", type = IdType.AUTO)
	private Integer id;

	/**
	 * 订单编号
	 */
	@TableField("order_code")
	private String orderCode;

	/**
	 * 购买数量
	 */
	@TableField("number")
	private Integer number;

	/**
	 * 原价
	 */
	@TableField("origin_amount")
	private BigDecimal originAmount;

	/**
	 * 售价
	 */
	@TableField("sales_amount")
	private BigDecimal salesAmount;

	/**
	 * 总额(售价*数量)
	 */
	@TableField("amount")
	private BigDecimal amount;

	/**
	 * 商品编码
	 */
	@TableField("product_code")
	private String productCode;

	/**
	 * 商品名称
	 */
	@TableField("product_name")
	private String productName;

	/**
	 * 商品小图
	 */
	@TableField("product_image")
	private String productImage;

	/**
	 * 附加参数
	 */
	@TableField("attach")
	private String attach;
	
	/**
	 * 创建时间
	 */
	@TableField("create_time")
	private LocalDateTime createTime;

	/**
	 * 更新时间
	 */
	@TableField("update_time")
	private LocalDateTime updateTime;
}
