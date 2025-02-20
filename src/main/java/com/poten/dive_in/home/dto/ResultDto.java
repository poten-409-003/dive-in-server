package com.poten.dive_in.home.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ResultDto {
    private String keyword;
    private String content;
    private String categoryName;
    private String address;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    public static ResultDto ofEntity(String keyword, String content, String categoryName, String address, LocalDateTime createdAt) {
        return ResultDto.builder()
                .keyword(keyword)
                .content(content)
                .categoryName(categoryName)
                .address(address)
                .createdAt(createdAt)
                .build();
    }
}
