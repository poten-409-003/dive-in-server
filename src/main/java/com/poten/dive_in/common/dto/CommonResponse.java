package com.poten.dive_in.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class CommonResponse<T> {

    private boolean success;
    private String message;
    private T data;

    public static <T> CommonResponse<T> success(final String message) {
        return CommonResponse.<T>builder()
                .data(null)
                .success(true)
                .message(message)
                .build();
    }

    public static <T> CommonResponse<T> success(final String message, final T data) {
        return CommonResponse.<T>builder()
                .data(data)
                .success(true)
                .message(message)
                .build();
    }

    public static <T> CommonResponse<T> error(final String message) {
        return CommonResponse.<T>builder()
                .data(null)
                .success(false)
                .message(message)
                .build();
    }

    public static <T> CommonResponse<T> error(final String message, T data) {
        return CommonResponse.<T>builder()
                .data(data)
                .success(false)
                .message(message)
                .build();
    }
}