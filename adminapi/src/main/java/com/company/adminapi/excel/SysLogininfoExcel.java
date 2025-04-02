package com.company.adminapi.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.company.adminapi.easyexcel.converter.SysUserConverter;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户登录日志
 *
 * @author CodeGenerator
 * @date 2023-04-14
 */
@Data
public class SysLogininfoExcel {

	/**
	 * id
	 */
	@ExcelProperty(value = "id")
	private Integer id;

	/**
	 * sys_user.id
	 */
	@ExcelProperty(value = "登录用户", converter = SysUserConverter.class)
	private Integer sysUserId;

	/**
	 * 登录时间
	 */
	@ExcelProperty(value = "登录时间")
	private LocalDateTime loginTime;

	/**
	 * 登录账户
	 */
	@ExcelProperty(value = "登录账户")
	private String account;

	/**
	 * 设备(ADMIN:admin端)
	 */
	@ExcelProperty(value = "设备(ADMIN:admin端)")
	private String device;

	/**
	 * 平台
	 */
	@ExcelProperty(value = "平台")
	private String platform;

	/**
	 * 操作系统
	 */
	@ExcelProperty(value = "操作系统")
	private String operator;

	/**
	 * 版本
	 */
	@ExcelProperty(value = "版本")
	private String version;

	/**
	 * 设备ID
	 */
	@ExcelProperty(value = "设备ID")
	private String deviceid;

	/**
	 * 渠道
	 */
	@ExcelProperty(value = "渠道")
	private String channel;

	/**
	 * ip
	 */
	@ExcelProperty(value = "ip")
	private String ip;

	/**
	 * 登录地区(根据ip获得)
	 */
	@ExcelProperty(value = "登录地区(根据ip获得)")
	private String address;

	/**
	 * 来源
	 */
	@ExcelProperty(value = "来源")
	private String source;

	/**
	 * 语言
	 */
	@ExcelProperty(value = "语言")
	private String lang;

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
