package com.company.order.api.feign;

import feign.FeignException;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.cloud.openfeign.FeignCircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.company.common.api.Result;
import com.company.order.api.constant.Constants;
import com.company.order.api.request.RegisterOrderReq;
import com.company.order.api.response.OrderDetailResp;

import java.util.Arrays;
import java.util.Collections;

@FeignClient(value = Constants.FEIGNCLIENT_VALUE, path = "/feignTest", fallbackFactory = FeignTestFeign.FeignTestFeignFallback.class)
public interface FeignTestFeign {

    @GetMapping("/getnoparam")
    OrderDetailResp getnoparam();

    @GetMapping("/getparam")
    OrderDetailResp getparam(@RequestParam("orderCode") String orderCode);

    @PostMapping("/postbody")
    OrderDetailResp postbody(@RequestBody RegisterOrderReq registerOrderReq);

    @Component
    public class FeignTestFeignFallback implements FallbackFactory<FeignTestFeign> {

        @Override
        public FeignTestFeign create(final Throwable e) {
            return new FeignTestFeign() {

                @Override
                public OrderDetailResp getnoparam() {
                    return Result.onFallbackError();
                }

                @Override
                public OrderDetailResp getparam(String orderCode) {
                    System.out.println("getparam fallback:" + e);

                    Throwable cause = e.getCause();
                    try {
                        if (cause != null) {
                            throw cause;
                        }
                        throw e;
                    } catch (Throwable exc) {
                        throw new RuntimeException(exc);
                    }
//                    return Result.onFallbackError();
                }

                @Override
                public OrderDetailResp postbody(RegisterOrderReq registerOrderReq) {
                    return Result.onFallbackError();
                }
            };
        }
    }
}
