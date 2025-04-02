package com.company.adminapi.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.company.adminapi.easyexcel.converter.UserInfoConverter;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 操作日志记录
 *
 * @author CodeGenerator
 * @date 2023-04-14
 */
@Data
public class UserOperLogExcel {

	/**
	 * id
	 */
	@ExcelProperty(value = "id")
	private Integer id;

	/**
	 * user_info.id
	 */
	@ExcelProperty(value = "用户UID", converter = UserInfoConverter.class)
	private Integer userId;

	/**
	 * 模块标题
	 */
	@ExcelProperty(value = "模块标题")
	private String title;

	/**
	 * 业务类型(0:其它,1:新增,2:修改,3:删除,4:查询)
	 */
	@ExcelProperty(value = "业务类型(0:其它,1:新增,2:修改,3:删除,4:查询)")
	private Integer businessType;

	/**
	 * 方法名称
	 */
	@ExcelProperty(value = "方法名称")
	private String method;

	/**
	 * 请求方式
	 */
	@ExcelProperty(value = "请求方式")
	private String requestMethod;

	/**
	 * 请求URL
	 */
	@ExcelProperty(value = "请求URL")
	private String operUrl;

	/**
	 * 主机地址
	 */
	@ExcelProperty(value = "主机地址")
	private String operIp;

	/**
	 * 操作地点
	 */
	@ExcelProperty(value = "操作地点")
	private String operLocation;

	/**
	 * 请求参数
	 */
	@ExcelProperty(value = "请求参数")
	private String operParam;

	/**
	 * 返回参数
	 */
	@ExcelProperty(value = "返回参数")
	private String jsonResult;

	/**
	 * 操作状态(0:正常,1:异常)
	 */
	@ExcelProperty(value = "操作状态(0:正常,1:异常)")
	private Integer status;

	/**
	 * 错误消息
	 */
	@ExcelProperty(value = "错误消息")
	private String errorMsg;

	/**
	 * 耗时(毫秒)
	 */
	@ExcelProperty(value = "耗时(毫秒)")
	private Integer costTime;

	/**
	 * 操作时间
	 */
	@ExcelProperty(value = "操作时间")
	private LocalDateTime operTime;

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
