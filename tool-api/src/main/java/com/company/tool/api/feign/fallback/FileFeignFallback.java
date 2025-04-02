package com.company.tool.api.feign.fallback;

import org.springframework.stereotype.Component;

import com.company.common.api.Result;
import com.company.tool.api.feign.FileFeign;
import com.company.tool.api.request.UploadReq;
import com.company.tool.api.response.UploadResp;

import org.springframework.cloud.openfeign.FallbackFactory;

@Component
public class FileFeignFallback implements FallbackFactory<FileFeign> {

	@Override
	public FileFeign create(final Throwable e) {
		return new FileFeign() {

			@Override
			public Result<UploadResp> upload(UploadReq uploadReq) {
				return Result.onFallbackError();
			}
		};
	}
}
