package com.poten.dive_in.community.post.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class PostResponseDTO {
    private Long postId;
    private String title;
    private String content;
    private String opengraph; // OpenGraph 정보 (필요 시 추가)
    private List<String> images; // 이미지 URL 리스트
    private Integer likesCnt; // 좋아요 수
    private Integer cmmtCnt; // 댓글 수
    private String writer; // 작성자 이름
    private String writerProfile; // 작성자 프로필 이미지 URL
}
