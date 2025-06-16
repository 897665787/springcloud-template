package com.company.common.exception;

import com.company.common.api.ResultCode;

/**
 * 未授权异常
 *
 * @author JQ棣
 */
public class UnauthorizedException extends BusinessException {

    public UnauthorizedException() {
        super(ResultCode.UNAUTHORIZED.getCode(), ResultCode.UNAUTHORIZED.getMessage());
    }
}
