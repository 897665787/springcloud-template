package com.company.tool.api.feign;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.company.common.api.Result;
import com.company.tool.api.feign.fallback.SubscribeFeignFallback;
import com.company.tool.api.request.SubscribeGrantReq;
import com.company.tool.api.request.SubscribeSendReq;

@FeignClient(value = "template-tool", path = "/subscribe", fallbackFactory = SubscribeFeignFallback.class)
public interface SubscribeFeign {

	@GetMapping("/selectTemplateCodeByGroup")
	Result<List<String>> selectTemplateCodeByGroup(@RequestParam("group") String group);

	/**
	 * 授权（如果实现了SubscribeType接口会同时发送消息）
	 * 
	 * @param subscribeGrantReq
	 * @return
	 */
	@PostMapping("/grant")
	Result<Void> grant(@RequestBody SubscribeGrantReq subscribeGrantReq);

	/**
	 * 发送
	 * 
	 * @param subscribeSendReq
	 * @return
	 */
	@PostMapping("/send")
	Result<Void> send(@RequestBody SubscribeSendReq subscribeSendReq);

	@GetMapping("/select4PreTimeSend")
	Result<List<Integer>> select4PreTimeSend(@RequestParam("limit") Integer limit);

	@GetMapping("/exePreTimeSend")
	Result<Void> exePreTimeSend(@RequestParam("id") Integer id);

	@GetMapping("/syncTemplate")
	Result<Void> syncTemplate();
}
