package com.company.admin.entity.interact.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 收藏结果
 * 
 * Created by JQ棣 on 2018/05/17.
 */
@Accessors(chain = true)
@Getter
@Setter
public class CollectResult {

	private Integer collected;
	private Integer collectNum;

}
