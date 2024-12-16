package com.poten.dive_in.community.post.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
public class PostRequestDTO {
    private String categoryType; // 카테고리 타입
    private String title; // 제목
    private String content; // 본문
    private List<MultipartFile> images; // 이미지 리스트
    private Boolean isContainsUrl; // URL 포함 여부
}
