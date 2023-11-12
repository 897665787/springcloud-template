package com.company.app.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "Swagger测试响应体")
public class SwaggerResp {

	@ApiModelProperty(value = "id")
	String id;

	@ApiModelProperty(value = "姓名")
	String name;

	@ApiModelProperty(value = "性别(0:男,1:女)")
	Integer sex;
}
