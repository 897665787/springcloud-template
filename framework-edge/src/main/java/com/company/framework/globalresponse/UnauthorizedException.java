package com.company.framework.globalresponse;

import com.feiniaojin.gracefulresponse.api.ExceptionAliasFor;

/**
 * 未授权异常
 *
 * @author JQ棣
 */
@ExceptionAliasFor(code = "401", msg = "未授权，请登录", aliasFor = UnauthorizedException.class, httpStatusCode = 401) // 可根据前端需求看是否使用http状态码来表示未登录，如果要使用状态码200，则去掉httpStatusCode=401
public class UnauthorizedException extends RuntimeException {
}
