package com.company.framework.gracefulresponse.feign;

import com.feiniaojin.gracefulresponse.EnableGracefulResponse;
import com.feiniaojin.gracefulresponse.GracefulResponseProperties;
import feign.codec.Decoder;
import feign.optionals.OptionalDecoder;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.HttpMessageConverterCustomizer;
import org.springframework.cloud.openfeign.support.ResponseEntityDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 参考FeignClientsConfiguration
 */
@Configuration
@EnableGracefulResponse
public class GracefulResponseFeignClientsConfiguration {

    /**
     * 替换默认的feignDecoder，使得返回值可以适配GracefulResponse
     */
    @Bean
    @ConditionalOnMissingBean
    public Decoder feignDecoder(ObjectFactory<HttpMessageConverters> messageConverters, ObjectProvider<HttpMessageConverterCustomizer> customizers, GracefulResponseProperties gracefulResponseProperties) {
        return new OptionalDecoder(new ResponseEntityDecoder(new GracefulResponseDecoder(messageConverters, customizers, gracefulResponseProperties)));
    }
}
