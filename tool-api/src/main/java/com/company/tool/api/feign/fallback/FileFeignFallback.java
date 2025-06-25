package com.company.tool.api.feign.fallback;

import com.company.common.api.Result;
import com.company.tool.api.feign.FileFeign;
import com.company.tool.api.request.ClientUploadReq;
import com.company.tool.api.request.UploadReq;
import com.company.tool.api.response.ClientUploadResp;
import com.company.tool.api.response.UploadResp;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class FileFeignFallback implements FallbackFactory<FileFeign> {

    @Override
    public FileFeign create(final Throwable e) {
        return new FileFeign() {

            @Override
            public UploadResp upload(UploadReq uploadReq) {
                return Result.onFallbackError();
            }

            @Override
            public ClientUploadResp clientUpload(ClientUploadReq clientUploadReq) {
                return Result.onFallbackError();
            }

            @Override
            public String presignedUrl(String fileKey) {
                return Result.onFallbackError();
            }
        };
    }
}
