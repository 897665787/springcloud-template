
package com.company.tool.api.feign;

import com.company.common.api.Result;
import com.company.tool.api.constant.Constants;
import com.company.tool.api.feign.fallback.PushFeignFallback;
import com.company.tool.api.request.BindDeviceReq;
import com.company.tool.api.request.SendPushReq;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = Constants.FEIGNCLIENT_VALUE, path = "/push", fallbackFactory = PushFeignFallback.class)
public interface PushFeign {

	@GetMapping("/select4PreTimeSend")
	Result<List<Integer>> select4PreTimeSend(@RequestParam("limit") Integer limit);

	@GetMapping("/exePreTimeSend")
	Result<Void> exePreTimeSend(@RequestParam("id") Integer id);

	@PostMapping("/bindDevice")
	Result<Void> bindDevice(@RequestBody BindDeviceReq bindDeviceReq);

	@PostMapping("/send")
	Result<Void> send(@RequestBody SendPushReq sendPushReq);
}
