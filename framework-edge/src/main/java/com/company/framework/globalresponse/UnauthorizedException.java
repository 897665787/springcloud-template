package com.company.framework.globalresponse;

import com.feiniaojin.gracefulresponse.api.ExceptionMapper;

/**
 * 未授权异常
 *
 * @author JQ棣
 */
@ExceptionMapper(code = "401", msg = "未授权，请登录")
public class UnauthorizedException extends RuntimeException {
}
