package com.poten.dive_in.home.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ResultDto {
    private String keyword;
    private String contentTitle; // 검색 결과 제목 (글 제목, 수영장/수업 이름)
    private String categoryName;
    private String address;
    private String contentSummary; // 내용 요약 또는 일부 내용
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    public static ResultDto ofEntity(String keyword, String contentTitle, String categoryName, String address, LocalDateTime createdAt, String contentSummary) {
        return ResultDto.builder()
                .keyword(keyword)
                .contentTitle(contentTitle)
                .categoryName(categoryName)
                .address(address)
                .contentSummary(contentSummary)
                .createdAt(createdAt)
                .build();
    }
}
