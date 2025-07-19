package com.company.user.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 钱包流水
 */
@Data
@Accessors(chain = true)
@TableName("bu_wallet_record")
public class WalletRecord {
	/**
	 * ID
	 */
	private Integer id;
	/**
	 * 唯一码(用于幂等，防止重复入出账)
	 */
	private String uniqueCode;
	/**
	 * bu_wallet.id
	 */
	private Integer walletId;
	/**
	 * 金额(元)<正数>
	 */
	private BigDecimal amount;
	/**
	 * 类型(1:入账,2:出账)
	 */
	private Integer type;
	/**
	 * 变化前的余额(元)
	 */
	private BigDecimal balanceBefore;
	/**
	 * 变化后的余额(元)
	 */
	private BigDecimal balanceAfter;
	/**
	 * 附加信息
	 */
	private String attach;

	private String remark;
	private LocalDateTime createTime;
	private LocalDateTime updateTime;
}
