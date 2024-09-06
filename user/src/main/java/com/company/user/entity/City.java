package com.company.user.entity;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@TableName("sc_city")
public class City {
	private Long id;
	private Long provinceId;
	private String name;
	private Integer status;
	private Integer seq;
	private LocalDateTime createTime;
	private LocalDateTime updateTime;
}
