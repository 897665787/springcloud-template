package com.company.tool.api.feign.fallback;

import org.springframework.stereotype.Component;

import com.company.common.api.Result;
import com.company.tool.api.feign.PopupFeign;
import com.company.tool.api.request.BestPopupReq;
import com.company.tool.api.request.CancelUserPopupReq;
import com.company.tool.api.request.CreateUserPopupReq;
import com.company.tool.api.response.BestPopupResp;

import org.springframework.cloud.openfeign.FallbackFactory;

@Component
public class PopupFeignFallback implements FallbackFactory<PopupFeign> {

	@Override
	public PopupFeign create(final Throwable e) {
		return new PopupFeign() {
			@Override
			public BestPopupResp bestPopup(BestPopupReq bestPopupReq) {
				return Result.onFallbackError();
			}

			@Override
			public Void createUserPopup(CreateUserPopupReq createUserPopupReq) {
				return Result.onFallbackError();
			}

			@Override
			public Void cancelUserPopup(CancelUserPopupReq cancelUserPopupReq) {
				return Result.onFallbackError();
			}

			@Override
			public Void remarkPopupLog(Integer popupLogId, String remark) {
				return Result.onFallbackError();
			}
		};
	}
}
