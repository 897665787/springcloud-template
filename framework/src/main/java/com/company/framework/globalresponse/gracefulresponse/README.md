# GracefulResponse 模块

## 概述

GracefulResponse 模块是基于 [GracefulResponse框架](https://doc.feiniaojin.com/graceful-response/home.html) 构建的统一响应处理组件，用于在 Spring Cloud 微服务架构中提供统一的 API 响应格式和异常处理机制。

## 功能特性

### 1. 统一响应格式
- 提供统一的响应数据结构（包含 code、msg、data 字段）
- 支持多种响应风格（Style0 和 Style1）
- 可自定义成功和失败的响应码及提示信息

### 2. HTTP 消息转换
- 通过 [GracefulResponseHttpMessageConverter](feign/converter/GracefulResponseHttpMessageConverter.java) 处理 HTTP 请求/响应转换
- 自动解析远程服务返回的统一响应格式
- 提取实际业务数据并转换为预期类型

### 3. Feign 客户端集成
- 通过 [GracefulResponseFeignClientsConfiguration](feign/GracefulResponseFeignClientsConfiguration.java#L22-L40) 配置类自动配置 Feign 客户端
- 使用 [GracefulResponseDecoder](feign/GracefulResponseDecoder.java#L26-L89) 解码器处理远程服务响应
- 确保 Feign 调用结果符合预期的数据类型

### 4. 异常处理
- 通过 [GracefulResponseFeignExceptionAspect](feign/GracefulResponseFeignExceptionAspect.java#L20-L44) 切面处理 Feign 调用异常
- 自动识别并抛出远程服务返回的业务异常
- 通过 [GracefulResponseExceptionContext](context/GracefulResponseExceptionContext.java#L12-L27) 管理异常上下文

## 配置

模块使用 application-gracefulresponse.yml 进行配置：
- `default-success-code`: 自定义成功响应码（默认为 0）
- `default-error-code`: 自定义失败响应码（默认为 1）
- `response-style`: 响应风格（0 或 1）
- `exclude-packages`: 例外包路径，该路径下的 controller 将被忽略处理

## 使用方式

在微服务项目中引入该模块后，所有 API 调用和 Feign 客户端调用将自动应用统一的响应处理机制，无需额外配置。

## 设计原理

该模块解决了微服务架构中不同服务返回格式不一致的问题，通过统一的响应格式和异常处理机制，简化了客户端对远程服务调用的处理逻辑。

## QA
关于String类型不包装问题，官方给了合理解释 https://doc.feiniaojin.com/graceful-response/qa.html