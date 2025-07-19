package com.company.tool.api.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.company.common.api.Result;
import com.company.tool.api.constant.Constants;
import com.company.tool.api.feign.fallback.PopupFeignFallback;
import com.company.tool.api.request.BestPopupReq;
import com.company.tool.api.request.CancelUserPopupReq;
import com.company.tool.api.request.CreateUserPopupReq;
import com.company.tool.api.response.BestPopupResp;

@FeignClient(value = Constants.FEIGNCLIENT_VALUE, path = "/popup", fallbackFactory = PopupFeignFallback.class)
public interface PopupFeign {

	@RequestMapping("/bestPopup")
	Result<BestPopupResp> bestPopup(@RequestBody BestPopupReq bestPopupReq);

	@RequestMapping("/createUserPopup")
	Result<Void> createUserPopup(@RequestBody CreateUserPopupReq createUserPopupReq);

	@RequestMapping("/cancelUserPopup")
	Result<Void> cancelUserPopup(@RequestBody CancelUserPopupReq cancelUserPopupReq);

	@RequestMapping("/remarkPopupLog")
	Result<Void> remarkPopupLog(@RequestParam("popupLogId") Integer popupLogId, @RequestParam("remark") String remark);
}
