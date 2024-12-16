package com.poten.dive_in.community.comment.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CommentResponseDTO {
    private Long cmmtId; // 댓글 ID
    private String content; // 댓글 내용
    private String writer; // 작성자 이름
    private String writerProfile; // 작성자 프로필 이미지 URL
    private Integer likeCnt; // 좋아요 수
    private String createdAt; // 생성 날짜
}
