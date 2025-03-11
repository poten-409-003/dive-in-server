package com.poten.dive_in.home.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ResultDto {
    private String title; // 검색 결과 제목 (글 제목, 수영장명, 수업명)
    private String content; // 검색 결과 본문 (글 내용, 수영장 위치, 수업 키워드)
    private String categoryName; // 커뮤니티의 경우 커뮤니티의 카테고리, 그 외 수영장, 수영수업
    private String contentSummary; // 키워드 포함 일부 내용 (30자)
    private String dataUrl; //각 상세 페이지 이동 url

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    public static ResultDto ofEntity(String title, String content, String categoryName, LocalDateTime createdAt, String contentSummary, String url) {
        return ResultDto.builder()
                .title(title)
                .content(content)
                .categoryName(categoryName)
                .contentSummary(contentSummary)
                .dataUrl(url)
                .createdAt(createdAt)
                .build();
    }
}
