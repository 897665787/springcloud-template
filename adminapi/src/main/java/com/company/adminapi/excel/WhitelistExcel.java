package com.company.adminapi.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 白名单
 *
 * @author CodeGenerator
 * @date 2023-04-14
 */
@Data
public class WhitelistExcel {

	/**
	 * id
	 */
	@ExcelProperty(value = "id")
	private Integer id;

	/**
	 * 用户id(user_info.id)
	 */
	@ExcelProperty(value = "用户id(user_info.id)")
	private Integer userId;

	/**
	 * 账户类型(coin:稳定币,wallet:法币)
	 */
	@ExcelProperty(value = "账户类型(coin:稳定币,wallet:法币)")
	private String accountType;

	/**
	 * 每日总上限(单位元)
	 */
	@ExcelProperty(value = "每日总上限(单位元)")
	private BigDecimal perDayLimit;

	/**
	 * 每日单笔上限(单位元)
	 */
	@ExcelProperty(value = "每日单笔上限(单位元)")
	private BigDecimal perDayPerOrderLimit;

	/**
	 * 每月总上限(单位元)
	 */
	@ExcelProperty(value = "每月总上限(单位元)")
	private BigDecimal perMonthLimit;

	/**
	 * 每年总上限(单位元)
	 */
	@ExcelProperty(value = "每年总上限(单位元)")
	private BigDecimal perYearLimit;

	/**
	 * 开始生效时间
	 */
	@ExcelProperty(value = "开始生效时间")
	private LocalDateTime effectTimeStart;

	/**
	 * 备注
	 */
	@ExcelProperty(value = "备注")
	private String remark;

	/**
	 * 创建时间
	 */
	@ExcelProperty(value = "创建时间")
	private LocalDateTime createTime;

	/**
	 * 更新时间
	 */
	@ExcelProperty(value = "更新时间")
	private LocalDateTime updateTime;

}
