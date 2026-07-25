package com.mangdehenzhi.exception;

import lombok.Getter;

/**
 * 业务异常 — 携带统一 ErrorCode
 */
@Getter
public class BusinessException extends RuntimeException {
    private final int code;
    private final int errorCode;

    public BusinessException(String message) {
        super(message);
        this.code = 500;
        this.errorCode = ErrorCode.INTERNAL_ERROR.getCode();
    }

    public BusinessException(int httpStatus, String message) {
        super(message);
        this.code = httpStatus;
        this.errorCode = httpStatus * 10;
    }

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code = httpStatusFromErrorCode(errorCode);
        this.errorCode = errorCode.getCode();
    }

    public BusinessException(ErrorCode errorCode, String detail) {
        super(detail != null ? errorCode.getMessage() + ": " + detail : errorCode.getMessage());
        this.code = httpStatusFromErrorCode(errorCode);
        this.errorCode = errorCode.getCode();
    }

    public BusinessException(int httpStatus, ErrorCode errorCode, String detail) {
        super(detail != null ? errorCode.getMessage() + ": " + detail : errorCode.getMessage());
        this.code = httpStatus;
        this.errorCode = errorCode.getCode();
    }

    private static int httpStatusFromErrorCode(ErrorCode ec) {
        int c = ec.getCode();
        if (c >= 2000 && c < 3000) return 401;
        if (c >= 3000 && c < 4000) return 400;
        if (c >= 4000 && c < 5000) return 400;
        if (c >= 5000 && c < 6000) return 404;
        if (c >= 6000 && c < 7000) return 400;
        if (c >= 7000 && c < 8000) return 404;
        if (c >= 8000 && c < 9000) return 409;
        return 500;
    }
}