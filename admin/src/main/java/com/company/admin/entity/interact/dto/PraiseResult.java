package com.company.admin.entity.interact.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 点赞结果
 * 
 * Created by JQ棣 on 2018/05/17.
 */
@Accessors(chain = true)
@Getter
@Setter
public class PraiseResult {

	private Integer praised;
	private Integer praiseNum;

}
