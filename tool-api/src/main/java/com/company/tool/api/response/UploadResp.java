package com.company.tool.api.response;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UploadResp {
	private String url;
}
