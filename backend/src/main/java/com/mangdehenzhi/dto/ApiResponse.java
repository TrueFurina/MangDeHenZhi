package com.mangdehenzhi.dto;

import com.mangdehenzhi.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
    private int code;
    private int errorCode;
    private String message;
    private T data;

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(200, 0, "success", data);
    }

    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(200, 0, message, data);
    }

    public static <T> ApiResponse<T> error(int httpStatus, String message) {
        return new ApiResponse<>(httpStatus, httpStatus * 10, message, null);
    }

    public static <T> ApiResponse<T> error(int httpStatus, int errorCode, String message) {
        return new ApiResponse<>(httpStatus, errorCode, message, null);
    }

    public static <T> ApiResponse<T> error(int httpStatus, ErrorCode errorCode) {
        return new ApiResponse<>(httpStatus, errorCode.getCode(), errorCode.getMessage(), null);
    }

    public static <T> ApiResponse<T> error(int httpStatus, ErrorCode errorCode, String detail) {
        return new ApiResponse<>(httpStatus, errorCode.getCode(),
                detail != null ? errorCode.getMessage() + ": " + detail : errorCode.getMessage(), null);
    }
}