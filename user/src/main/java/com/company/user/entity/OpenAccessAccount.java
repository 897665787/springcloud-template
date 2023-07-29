package com.company.user.entity;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotations.TableName;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@TableName("open_access_account")
public class OpenAccessAccount {
	private Integer id;
	
	private String appid;
	private String appKey;
	
	private String remark;
	private LocalDateTime createTime;
	private LocalDateTime updateTime;
}
