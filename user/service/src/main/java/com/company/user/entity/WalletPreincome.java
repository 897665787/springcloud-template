package com.company.user.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 钱包待入账
 */
@Data
@Accessors(chain = true)
@TableName("bu_wallet_preincome")
public class WalletPreincome {
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
	 * 入账金额(元)<正数>
	 */
	private BigDecimal amount;
	/**
	 * 状态(1:未入账,2:已入账)
	 */
	private Integer status;
	/**
	 * 处理bean名称
	 */
	private String beanName;
	/**
	 * 附加信息
	 */
	private String attach;

	private String remark;
	private LocalDateTime createTime;
	private LocalDateTime updateTime;
}
