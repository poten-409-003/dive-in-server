package com.poten.dive_in.community.post.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
public class PostRequestDto {
    @NotBlank(message = "카테고리 선택은 필수입니다.")
    private String categoryType; // 카테고리 타입

    @NotBlank(message = "제목은 필수입니다.")
    @Size(max = 20, message = "20자 이하로만 작성할 수 있습니다.")
    private String title; // 제목

    @NotBlank(message = "본문은 필수입니다.")
    @Size(max = 2000, message = "2,000자 이하로만 작성할 수 있습니다.")
    private String content; // 본문

    @NotNull(message = "회원은 필수입니다.")
    private Long memberId;
}
