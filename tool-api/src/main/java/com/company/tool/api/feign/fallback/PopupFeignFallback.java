package com.company.tool.api.feign.fallback;

import org.springframework.stereotype.Component;

import com.company.common.api.Result;
import com.company.common.api.ResultCode;
import com.company.tool.api.feign.PopupFeign;
import com.company.tool.api.request.BestPopupReq;
import com.company.tool.api.request.CancelUserPopupReq;
import com.company.tool.api.request.CreateUserPopupReq;
import com.company.tool.api.response.BestPopupResp;

import feign.hystrix.FallbackFactory;

@Component
public class PopupFeignFallback implements FallbackFactory<PopupFeign> {

	@Override
	public PopupFeign create(final Throwable e) {
		return new PopupFeign() {
			@Override
			public Result<BestPopupResp> bestPopup(BestPopupReq bestPopupReq) {
				return Result.fail(ResultCode.FALLBACK);
			}

			@Override
			public Result<Void> createUserPopup(CreateUserPopupReq createUserPopupReq) {
				return Result.fail(ResultCode.FALLBACK);
			}

			@Override
			public Result<Void> cancelUserPopup(CancelUserPopupReq cancelUserPopupReq) {
				return Result.fail(ResultCode.FALLBACK);
			}

			@Override
			public Result<Void> remarkPopupLog(Integer popupLogId, String remark) {
				return Result.fail(ResultCode.FALLBACK);
			}
		};
	}
}
