package com.company.user.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotations.TableName;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 钱包
 */
@Data
@Accessors(chain = true)
@TableName("bu_wallet")
public class Wallet {
	/**
	 * ID
	 */
	private Integer id;
	/**
	 * bu_user_info.id
	 */
	private Integer userId;
	/**
	 * 类型(1~9表示总钱包,2位数表示子钱包)
	 */
	private Integer type;
	/**
	 * 余额(元)
	 */
	private BigDecimal balance;
	/**
	 * 状态(1:正常,2:风控中,3:冻结)
	 */
	private Integer status;

	private String remark;
	private LocalDateTime createTime;
	private LocalDateTime updateTime;
}
