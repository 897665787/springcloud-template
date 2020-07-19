package com.company.order.elasticsearch;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import lombok.Data;

@Data
@Document(indexName = "tmall_springboot", type = "product")
// indexName：索引名称 可以理解为数据库名 必须为小写不然会报org.elasticsearch.indices.InvalidIndexNameException异常
// type：类型 可以理解为表名
public class Product {

	@Id
	private Long id;
	
	private String orderCode;
}