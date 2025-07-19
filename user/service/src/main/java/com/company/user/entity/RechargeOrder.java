package com.company.user.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

/**
 * 充值订单表
 */
@Data
@TableName(value ="bu_recharge_order")
public class RechargeOrder {
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
     * 充值编码
     */
    @TableField(value = "recharge_code")
    private String rechargeCode;
    
    /**
     * 充值金额
     */
    @TableField(value = "amount")
    private BigDecimal amount;
    
    /**
     * 赠送金额
     */
    @TableField(value = "gift_amount")
    private BigDecimal giftAmount;

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