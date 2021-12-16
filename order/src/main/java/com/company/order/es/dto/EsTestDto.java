package com.company.order.es.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.frameworkset.orm.annotation.ESId;

import lombok.Data;

@Data
public class EsTestDto {

	@ESId // 属性作为文档唯一标识，根据ip值对应的索引文档存在与否来决定添加或者修改操作
	private String id;

	private String businessType;

	private String searchTxt;

	/**
	 * 访问量
	 */
	private Integer accessCount;

	/**
	 * 详细地址
	 */
	private String addr;
	/**
	 * 中心经度
	 */
	private BigDecimal longitude;
	/**
	 * 中心纬度
	 */
	private BigDecimal latitude;

	@JsonIgnore // 如果对象的属性不需要存入索引中，则在字段的定义加上@JsonIgnore注解
	private BigDecimal distance;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8") // 时间字段需要格式化再写入ES
	private Date updateDate;

	private String remark;
}
