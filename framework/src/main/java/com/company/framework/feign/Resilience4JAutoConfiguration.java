///*
// * Copyright 2013-2022 the original author or authors.
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *      https://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//
//package com.company.framework.feign;
//
//import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
//import io.github.resilience4j.timelimiter.TimeLimiterRegistry;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
//import org.springframework.boot.context.properties.EnableConfigurationProperties;
//import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory;
//import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigurationProperties;
//import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4jBulkheadProvider;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import java.util.concurrent.ThreadPoolExecutor;
//
///**
// * copy from org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JAutoConfiguration
// *
// * @author Ryan Baxter
// * @author Eric Bussieres
// * @author Andrii Bohutskyi
// */
//@Configuration(proxyBeanMethods = false)
//@EnableConfigurationProperties(Resilience4JConfigurationProperties.class)
//@ConditionalOnProperty(name = { "spring.cloud.circuitbreaker.resilience4j.enabled",
//        "spring.cloud.circuitbreaker.resilience4j.blocking.enabled" }, matchIfMissing = true)
//public class Resilience4JAutoConfiguration {
//
//    @Bean
//    public Resilience4JCircuitBreakerFactory resilience4jCircuitBreakerFactory(
//            CircuitBreakerRegistry circuitBreakerRegistry, TimeLimiterRegistry timeLimiterRegistry,
//            @Autowired(required = false) Resilience4jBulkheadProvider bulkheadProvider,
//            Resilience4JConfigurationProperties resilience4JConfigurationProperties,
//            ThreadPoolExecutor threadPoolExecutor) {
//        Resilience4JCircuitBreakerFactory factory = new Resilience4JCircuitBreakerFactory(circuitBreakerRegistry,
//                timeLimiterRegistry, bulkheadProvider, resilience4JConfigurationProperties);
//        factory.configureExecutorService(threadPoolExecutor);// 自定义线程池传递日志ID
//        return factory;
//    }
//
//}
