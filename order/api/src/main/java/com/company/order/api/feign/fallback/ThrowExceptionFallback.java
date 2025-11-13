package com.company.order.api.feign.fallback;

import com.company.common.api.ResultCode;
import com.company.common.exception.ResultException;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class ThrowExceptionFallback<Object> implements FallbackFactory<Object> {

    @Override
    public Object create(final Throwable e) {
        throw new ResultException(ResultCode.API_FUSING.getCode(), ResultCode.API_FUSING.getMessage());
    }
}
