package com.company.user.entity;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@TableName("bu_user_info")
public class UserInfo {
	private Integer id;
	
	private String nickname;
	private String avator;
	
	private String remark;
	private LocalDateTime createTime;
	private LocalDateTime updateTime;
}
