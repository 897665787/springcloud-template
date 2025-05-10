package com.company.app.controller;

import com.company.common.api.Result;
import com.company.tool.api.feign.PushFeign;
import com.company.tool.api.request.BindDeviceReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 推送API
 * 
 * @author JQ棣
 */
@RestController
@RequestMapping("/push")
public class PushController {

	@Autowired
	private PushFeign pushFeign;

	/**
	 * 绑定设备ID与推送平台的ID
	 */
	@RequestMapping("/bindDevice")
	public Result<Void> bindDevice(@RequestBody BindDeviceReq bindDeviceReq) {
		return pushFeign.bindDevice(bindDeviceReq);
	}
}
