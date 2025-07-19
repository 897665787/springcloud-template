package com.company.user.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 钱包入账记录使用记录
 */
@Data
@Accessors(chain = true)
@TableName(value = "bu_wallet_income_use_record")
public class WalletIncomeUseRecord {
	/**
	 * ID
	 */
	private Integer id;
	
	/**
	 * bu_wallet.id
	 */
	private Integer walletId;

	/**
	 * bu_wallet_record.id
	 */
	private Integer walletRecordId;

	/**
	 * 未使用金额
	 */
	private BigDecimal unusedAmount;

	/**
	 * 入账金额(元)<正数>
	 */
	private BigDecimal amount;

	/**
	 * 状态(1:未使用,2:部分使用,3:已使用,4:已失效)
	 */
	private Integer status;

	private LocalDateTime invalidTime;
	
	/**
	 * 处理bean名称
	 */
	private String beanName;

	private String remark;
	private LocalDateTime createTime;
	private LocalDateTime updateTime;
}