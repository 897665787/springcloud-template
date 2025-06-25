package com.company.im.api.feign.fallback;

import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import com.company.common.api.Result;
import com.company.im.api.feign.WebsocketFeign;
import com.company.im.api.request.AllReq;
import com.company.im.api.request.GroupReq;
import com.company.im.api.request.UserReq;

@Component
public class WebsocketFeignFallback implements FallbackFactory<WebsocketFeign> {

	@Override
	public WebsocketFeign create(final Throwable e) {
		return new WebsocketFeign() {
			@Override
			public Void sendToAll(AllReq allReq) {
				return Result.onFallbackError();
			}

			@Override
			public Void sendToUser(UserReq userReq) {
				return Result.onFallbackError();
			}

			@Override
			public Void sendToGroup(GroupReq groupReq) {
				return Result.onFallbackError();
			}
		};
	}
}
