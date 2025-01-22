package com.poten.dive_in.community.post.dto;

import com.poten.dive_in.common.service.DateTimeUtil;
import com.poten.dive_in.community.comment.dto.CommentResponseDTO;
import com.poten.dive_in.community.post.entity.Post;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class PostDetailResponseDto {
    private Long postId;
    private String categoryName;
    private String title;
    private String content;
    private List<PostImageDto> images;
    private Integer likesCnt;
    private Integer viewCnt;
    private Integer cmntCnt;
    private String writer;
    private String writerProfile;
    private String createdAt;
    private String updatedAt;
    private List<CommentResponseDTO> commentList;
    private Boolean isLiked;
    private Boolean isPopular; //TODO 인기글 표시 로직 추가

    public static PostDetailResponseDto ofEntity(Post post) {

        List<PostImageDto> postImageDtoList = (post.getImages() != null) ?
                post.getImages().stream()
                        .map(PostImageDto::ofEntity)
                        .collect(Collectors.toList())
                : new ArrayList<>();

        List<CommentResponseDTO> commentResponseDTOList = (post.getComments() != null) ?
                post.getComments().stream()
                        .map(CommentResponseDTO::ofEntity)
                        .collect(Collectors.toList())
                : new ArrayList<>();
        String updatedAtStr = null;
        LocalDateTime updatedAt = post.getUpdatedAt();
        if (!post.getCreatedAt().equals(updatedAt)) {
            updatedAtStr = DateTimeUtil.formatDateTimeToKorean(post.getUpdatedAt());
        }
        return PostDetailResponseDto.builder()
                .postId(post.getId())
                .categoryName(post.getCategoryCode().getCodeName())
                .title(post.getTitle())
                .content(post.getContent())
                .images(postImageDtoList)
                .likesCnt(post.getLikeCount())
                .viewCnt(post.getViewCount())
                .cmntCnt(post.getCommentCount())
                .writer(post.getMember().getNickname())
                .writerProfile(post.getMember().getProfileImageUrl())
                .createdAt(DateTimeUtil.formatDateTimeToKorean(post.getCreatedAt()))
                .updatedAt(updatedAtStr)
                .commentList(commentResponseDTOList)
                .isLiked(false) //isLiked TODO token 회원정보 확인 후 수정 필요
                .isPopular(false)
                .build();
    }

    public void assignIsLiked(Boolean isliked) {
        this.isLiked = isliked;
    }

    public void assignIsPopular(Boolean isPopular) {
        this.isPopular = isPopular;
    }
}
