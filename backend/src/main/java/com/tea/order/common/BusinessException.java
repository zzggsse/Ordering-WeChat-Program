package com.tea.order.common;

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {
    private final int code;

    public BusinessException(String message) {
        this(ResultCode.BAD_REQUEST.getCode(), message);
    }
    public BusinessException(ResultCode resultCode) {
        this(resultCode.getCode(), resultCode.getMessage());
    }
    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
    }
}
