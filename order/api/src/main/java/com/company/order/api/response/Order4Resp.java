package com.company.order.api.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Order4Resp {
	/**
     * ID
     */
    private Integer id;

    /**
     * user_info.id
     */
    private Integer userId;

    /**
     * 订单编号
     */
    private String orderCode;

    /**
     * 订单类型(buy_member:开通会员,distribute:配送类,writeoff:核销码)
     */
    private String orderType;

    /**
     * 状态(1:待支付,2:已取消(END)3:已支付[待发货],4:待收货,5.已完成(END),6:退款(END))
     */
    private Integer status;

    /**
     * 子状态(11:待支付,21:已取消(END),31:待发货,32:发货中,33:发货失败,41:已发货,51:待评价,52:已结束(END),61:待审核,61:退款中,62:退款成功(END),63:退款失败,64:部分退款成功(END))
     */
    private Integer subStatus;

    /**
     * 商品总额
     */
    private BigDecimal productAmount;

    /**
     * 订单总额(元，商品金额+各种费用总和)
     */
    private BigDecimal orderAmount;
    
    /**
     * 抵扣总额(元，各种优惠、扣减总和)
     */
    private BigDecimal reduceAmount;
    
    /**
     * 需付金额(元)
     */
    private BigDecimal needPayAmount;

    /**
     * 实付金额
     */
    private BigDecimal payAmount;

    /**
     * 支付时间
     */
    private LocalDateTime payTime;
    
	/**
	 * 完成时间
	 */
	private LocalDateTime finishTime;

    /**
     * 退款金额(多次求和)
     */
    private BigDecimal refundAmount;

    /**
     * 退款时间(最后1次)
     */
    private LocalDateTime refundTime;
    
    /**
     * 用户删除(1:未删除,2:已删除)
     */
    private Integer userDel;
    
    /**
     * 子订单查询地址
     */
    private String subOrderUrl;
	
	/**
	 * 附加参数
	 */
	private String attach;

    /**
     * 备注(多个使用/分隔)
     */
    private String remark;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}