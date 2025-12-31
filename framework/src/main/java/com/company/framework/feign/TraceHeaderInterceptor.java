package com.company.framework.feign;

import com.company.framework.trace.TraceManager;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * feign调用过程中传递traceId
 */
@Component
@RequiredArgsConstructor
public class TraceHeaderInterceptor implements RequestInterceptor {

    private final TraceManager traceManager;

    @Override
    public void apply(RequestTemplate template) {
        traceManager.headers().forEach((template::header));
    }
}
