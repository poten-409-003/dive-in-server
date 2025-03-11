package com.poten.dive_in.community.comment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentRequestDTO {
    @NotBlank(message = "내용은 필수입니다.")
    @Size(max = 500, message = "500자 이하로만 작성할 수 있습니다.")
    private String content; // 댓글 내용

    @NotNull(message = "글은 필수입니다.")
    private Long postId; // 게시글 ID

}
