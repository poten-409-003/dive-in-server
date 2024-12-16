package com.poten.dive_in.community.comment.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentRequestDTO {
    private String content; // 댓글 내용
    private Long postId; // 게시글 ID
    private Long memberId; // 작성자 ID
}
