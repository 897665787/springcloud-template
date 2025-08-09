package com.company.order.api.feign;

import com.company.common.api.Result;
import com.company.order.api.constant.Constants;
import com.company.order.api.request.RegisterOrderReq;
import com.company.order.api.response.OrderDetailResp;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = Constants.FEIGNCLIENT_VALUE, path = "/feignTest", fallbackFactory = FeignTestFeign.FeignTestFeignFallback.class)
public interface FeignTestFeign {

    @GetMapping("/getnoparam")
    Result<OrderDetailResp> getnoparam();

    @GetMapping("/getparam")
    Result<OrderDetailResp> getparam(@RequestParam("orderCode") String orderCode);

    @PostMapping("/postbody")
    Result<OrderDetailResp> postbody(@RequestBody RegisterOrderReq registerOrderReq);

    @GetMapping("/context")
    Result<OrderDetailResp> context();

    @Component
    public class FeignTestFeignFallback implements FallbackFactory<FeignTestFeign> {

        @Override
        public FeignTestFeign create(final Throwable e) {
            return new FeignTestFeign() {

                @Override
                public Result<OrderDetailResp> getnoparam() {
                    return Result.onFallbackError();
                }

                @Override
                public Result<OrderDetailResp> getparam(String orderCode) {
                    return Result.onFallbackError();
                }

                @Override
                public Result<OrderDetailResp> postbody(RegisterOrderReq registerOrderReq) {
                    return Result.onFallbackError();
                }

                @Override
                public Result<OrderDetailResp> context() {
                    return Result.onFallbackError();
                }
            };
        }
    }
}
