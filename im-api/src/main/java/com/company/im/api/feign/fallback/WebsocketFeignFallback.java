package com.company.im.api.feign.fallback;

import com.company.common.api.Result;
import com.company.im.api.feign.WebsocketFeign;
import com.company.im.api.request.AllReq;
import com.company.im.api.request.GroupReq;
import com.company.im.api.request.UserReq;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class WebsocketFeignFallback implements FallbackFactory<WebsocketFeign> {

	@Override
	public WebsocketFeign create(final Throwable e) {
		return new WebsocketFeign() {
			@Override
			public Result<Void> sendToAll(AllReq allReq) {
				return Result.onFallbackError();
			}

			@Override
			public Result<Void> sendToUser(UserReq userReq) {
				return Result.onFallbackError();
			}

			@Override
			public Result<Void> sendToGroup(GroupReq groupReq) {
				return Result.onFallbackError();
			}
		};
	}
}
