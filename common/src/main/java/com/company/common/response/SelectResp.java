package com.company.common.response;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
public class SelectResp<T> {
	private String code;// 总量
	private String name;// 数据
}
