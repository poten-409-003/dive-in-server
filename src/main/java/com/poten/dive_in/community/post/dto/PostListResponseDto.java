package com.poten.dive_in.community.post.dto;

import com.poten.dive_in.common.service.DateTimeUtil;
import com.poten.dive_in.community.post.entity.Post;
import com.poten.dive_in.community.post.entity.PostImage;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Set;


@Getter
@Builder
public class PostListResponseDto {
    private Long postId;
    private String categoryName;
    private String title;
    private String content;
    private PostImageDto image;
    private Integer likesCnt;
    private Integer cmmtCnt;
    private Integer viewCnt;
    private String writer;
    private String writerProfile;
    private String createdAt;
    private String updatedAt;
    private Boolean isPopular; //TODO 인기글 표시 로직 추가

    public static PostListResponseDto ofEntity(Post post, Boolean isPopular) {
        Set<PostImage> images = post.getImages();

        PostImageDto postImageDto = (images != null && !images.isEmpty())
                ? images.stream()
                .filter(image -> "Y".equals(image.getIsRepresentative()))
                .findFirst()
                .map(PostImageDto::ofEntity)
                .orElse(null)
                : null;
        String updatedAtStr = null;
        LocalDateTime updatedAt = post.getUpdatedAt();
        if (!post.getCreatedAt().equals(updatedAt)) {
            updatedAtStr = DateTimeUtil.formatDateTimeToKorean(post.getUpdatedAt());
        }
        return PostListResponseDto.builder()
                .postId(post.getId())
                .categoryName(post.getCategoryCode().getCodeName())
                .title(post.getTitle())
                .content(truncateContent(post.getContent()))
                .image(postImageDto)
                .likesCnt(post.getLikeCount())
                .cmmtCnt(post.getCommentCount())
                .viewCnt(post.getViewCount())
                .writer(post.getMember().getNickname())
                .writerProfile(post.getMember().getProfileImageUrl())
                .createdAt(DateTimeUtil.formatDateTimeToKorean(post.getCreatedAt()))
                .updatedAt(updatedAtStr)
                .isPopular(isPopular)
                .build();
    }

    private static String truncateContent(String content) {
        if (content != null && content.length() > 30) {
            return content.substring(0, 30) + "...";
        }
        return content;
    }
}
