package com.company.order.api.request;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.company.order.api.enums.OrderEnum;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class OrderReq {
	/**
	 * 子订单事件
	 */
	private OrderEnum.SubOrderEventEnum subOrderEvent;
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
    private OrderEnum.StatusEnum status;

    /**
     * 子状态(11:待支付,21:已取消(END),31:待发货,32:发货中,33:发货失败,41:已发货,51:待评价,52:已结束(END),61:待审核,61:退款中,62:退款成功(END),63:退款失败,64:部分退款成功(END))
     */
    private OrderEnum.SubStatusEnum subStatus;

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
	 * 用户备注
	 */
	private String userRemark;
	
	/**
	 * 附加参数
	 */
	private String attach;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

	/**
	 * 商品列表
	 */
	private List<ProductReq> productList;

	@Data
	public static class ProductReq {
		/**
		 * 购买数量
		 */
		private Integer number;

		/**
		 * 原价
		 */
		private BigDecimal originAmount;

		/**
		 * 售价
		 */
		private BigDecimal salesAmount;

		/**
		 * 总额(售价*数量)
		 */
		private BigDecimal amount;

		/**
		 * 商品编码
		 */
		private String productCode;

		/**
		 * 商品名称
		 */
		private String productName;

		/**
		 * 商品小图
		 */
		private String productImage;
		
		/**
		 * 附加参数
		 */
		private String attach;
	}
}
