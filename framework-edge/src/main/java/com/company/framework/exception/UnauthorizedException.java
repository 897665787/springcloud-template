package com.company.framework.exception;

import com.company.common.api.ResultCode;
import com.company.common.exception.BusinessException;

/**
 * 未授权异常
 *
 * @author JQ棣
 */
public class UnauthorizedException extends BusinessException {
	private static final long serialVersionUID = 1L;

    public UnauthorizedException() {
        super(ResultCode.UNAUTHORIZED.getCode(), ResultCode.UNAUTHORIZED.getMessage());
    }
}
