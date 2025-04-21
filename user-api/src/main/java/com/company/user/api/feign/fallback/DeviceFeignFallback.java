package com.company.user.api.feign.fallback;

import com.company.common.api.Result;
import com.company.user.api.feign.DeviceFeign;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class DeviceFeignFallback implements FallbackFactory<DeviceFeign> {

	@Override
	public DeviceFeign create(final Throwable e) {
		return new DeviceFeign() {
			@Override
			public Result<Boolean> isOnline(String deviceid) {
				return Result.onFallbackError();
			}
		};
	}
}
