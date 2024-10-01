package com.poten.dive_in.dto;

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

    public static<T> CommonResponse<T> success(final String message){
        return CommonResponse.<T>builder()
                .data(null)
                .success(true)
                .message(message)
                .build();
    }

    public static<T> CommonResponse<T> success(final String message, final T t){
        return CommonResponse.<T>builder()
                .data(t)
                .success(true)
                .message(message)
                .build();
    }

    public static<T> CommonResponse<T> error(final String message){
        return CommonResponse.<T>builder()
                .data(null)
                .success(false)
                .message(message)
                .build();
    }
}