package com.company.user.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 钱包余额冻结
 */
@Data
@Accessors(chain = true)
@TableName("bu_wallet_freeze")
public class WalletFreeze {
	/**
	 * ID
	 */
	private Integer id;
	/**
	 * 唯一码
	 */
	private String uniqueCode;
	/**
	 * 订单号(用于归还)
	 */
	private String orderCode;
	/**
	 * bu_wallet.id
	 */
	private Integer walletId;
	/**
	 * 冻结金额(元)
	 */
	private BigDecimal freezeAmount;
	/**
	 * 状态(1:冻结中,2:已使用,3:已归还)
	 */
	private Integer status;

	private String remark;
	private LocalDateTime createTime;
	private LocalDateTime updateTime;
}
