package com.company.auth.authentication.impl.tool;

import com.company.auth.authentication.impl.tool.dto.MaMobile;

public interface IMaMobileTool {

	public MaMobile getMobileInfo(String appid, String code);
}
