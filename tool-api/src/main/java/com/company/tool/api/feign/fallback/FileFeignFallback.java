package com.company.tool.api.feign.fallback;

import org.springframework.stereotype.Component;

import com.company.common.api.Result;
import com.company.common.api.ResultCode;
import com.company.tool.api.feign.FileFeign;
import com.company.tool.api.request.UploadReq;

import feign.hystrix.FallbackFactory;

@Component
public class FileFeignFallback implements FallbackFactory<FileFeign> {

	@Override
	public FileFeign create(final Throwable e) {
		return new FileFeign() {

			@Override
			public Result<String> upload(UploadReq uploadReq) {
				return Result.fail(ResultCode.FALLBACK);
			}
		};
	}
}
