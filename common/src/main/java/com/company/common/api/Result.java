package com.company.common.api;

import com.company.common.exception.ResultException;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class Result<T> {
    private Integer code;// 响应码
    private String message;// 响应信息
    private T data;// 数据

    public Result<T> setResultCode(ResultCode resultCode) {
        this.code = resultCode.getCode();
        this.message = resultCode.getMessage();
        return this;
    }

    public static <T> Result<T> success() {
        return new Result<T>().setResultCode(ResultCode.SUCCESS);
    }

    public static <T> Result<T> success(T data) {
        return new Result<T>().setResultCode(ResultCode.SUCCESS).setData(data);
    }

    public static <T> Result<T> success(T data, String message) {
        return new Result<T>().setResultCode(ResultCode.SUCCESS).setMessage(message).setData(data);
    }

    public static <T> Result<T> fail(Integer code, String message) {
        return new Result<T>().setCode(code).setMessage(message);
    }

    public static <T> Result<T> fail(String message) {
        return new Result<T>().setResultCode(ResultCode.FAIL).setMessage(message);
    }

    public static <T> Result<T> fail(ResultCode resultCode) {
        return new Result<T>().setResultCode(resultCode);
    }

    public static <T> Result<T> fail(ResultCode resultCode, T data) {
        return new Result<T>().setResultCode(resultCode).setData(data);
    }

    public boolean successCode() {
        if (code == null || ResultCode.of(code) != ResultCode.SUCCESS) {
            return false;
        }
        return true;
    }

    public T dataOrThrow() {
        if (!successCode()) {
            throw new ResultException(code, message);
        }
        return data;
    }

    public static <T> Result<T> onFallbackError() {
        return new Result<T>().setResultCode(ResultCode.API_FUSING);
    }
}
