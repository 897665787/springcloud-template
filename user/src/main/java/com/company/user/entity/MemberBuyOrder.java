package com.company.user.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import lombok.Data;

/**
 * 购买会员订单表
 */
@Data
@TableName(value ="bu_member_buy_order")
public class MemberBuyOrder {
    /**
     * ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * bu_user_info.id
     */
    @TableField(value = "user_id")
    private Integer userId;

    /**
     * 订单编号
     */
    @TableField(value = "order_code")
    private String orderCode;

    /**
     * mk_user_coupon.id
     */
    @TableField(value = "user_coupon_id")
    private Integer userCouponId;

    /**
     * 会员续期编码
     */
    @TableField(value = "product_code")
    private String productCode;
    
    /**
     * 会员续期费用
     */
    @TableField(value = "amount")
    private BigDecimal amount;
    
    /**
     * 续期增加天数
     */
    @TableField(value = "add_days")
    private Integer addDays;
    
    /**
     * 退款手续费
     */
    @TableField(value = "refund_service_amount")
    private BigDecimal refundServiceAmount;

    /**
     * 备注(多个使用/分隔)
     */
    @TableField(value = "remark")
    private String remark;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time")
    private LocalDateTime updateTime;
}