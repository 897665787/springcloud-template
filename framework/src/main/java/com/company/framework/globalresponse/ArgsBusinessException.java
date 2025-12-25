package com.company.framework.globalresponse;

import com.feiniaojin.gracefulresponse.GracefulResponseException;

/**
 * 带参数的异常响应
 *
 * @author JQ棣
 */
public class ArgsBusinessException extends GracefulResponseException {

    /**
     * 参数
     */
    private Object[] args;

    public ArgsBusinessException(String msg, Object[] args) {
        super(msg);
        this.args = args;
    }

    public ArgsBusinessException(String code, String msg, Object[] args) {
        super(code, msg);
        this.args = args;
    }

    public Object[] getArgs() {
        return this.args;
    }
}
