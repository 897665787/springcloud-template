package com.company.app.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "Swagger测试请求参数")
public class SwaggerReq {

	@ApiModelProperty(value = "id")
	String id;

	@ApiModelProperty(value = "姓名")
	String name;
}
