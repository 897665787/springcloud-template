package com.company.order.es.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.frameworkset.orm.annotation.ESId;

import lombok.Data;

@Data
//@FieldDefaults(level = AccessLevel.PRIVATE)
public class Brand {

	@ESId // 属性作为文档唯一标识，根据ip值对应的索引文档存在与否来决定添加或者修改操作
	String id;

	String type;
	
	String brandCode;
	
	String name;

	/**
	 * 访问量
	 */
	Integer accessCount;

	/**
	 * 详细地址
	 */
	String addr;
	/**
	 * 中心经度
	 */
	BigDecimal longitude;
	/**
	 * 中心纬度
	 */
	BigDecimal latitude;

	@JsonIgnore // 如果对象的属性不需要存入索引中，则在字段的定义加上@JsonIgnore注解
	BigDecimal distance;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8") // 时间字段需要格式化再写入ES
	Date updateDate;

	String remark;

	List<Product> productList;

	@Data
	public static class Product {
		String code;

		String name;
		
		String short_name;
		
		String subtitle;
		
		@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8") // 时间字段需要格式化再写入ES
		Date updateDate;

		List<Address> addressList;

		@Data
		public static class Address {
			String province;
			String city;
			String district;
			String addr;
			
			@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8") // 时间字段需要格式化再写入ES
			Date updateDate;
		}
	}
}
