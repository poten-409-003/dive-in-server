package com.poten.dive_in.community.post.dto;

import com.poten.dive_in.common.service.DateTimeUtil;
import com.poten.dive_in.community.post.entity.Post;
import com.poten.dive_in.community.post.entity.PostImage;
import lombok.Builder;
import lombok.Getter;

import java.util.Optional;
import java.util.Set;


@Getter
@Builder
public class PostListResponseDto {
    private Long postId;
    private String title;
    private String content;
    private PostImageDto image;
    private Integer likesCnt;
    private Integer cmmtCnt;
    private Integer viewCnt;
    private String writer;
    private String writerProfile;
    private String createdAt;

    public static PostListResponseDto ofEntity(Post post){
        Set<PostImage> images = post.getImages();

        PostImageDto postImageDto = (images != null && !images.isEmpty())
                ? images.stream()
                .filter(image -> "Y".equals(image.getIsRepresentative())) // isRepresentative가 "Y"인 필터링
                .findFirst()
                .map(PostImageDto::ofEntity)
                .orElse(null)
                : null;

        return PostListResponseDto.builder()
                .postId(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .image(postImageDto)
                .likesCnt(post.getLikeCount())
                .cmmtCnt(post.getCommentCount())
                .viewCnt(post.getViewCount())
                .writer(post.getMember().getNickname())
                .writerProfile(post.getMember().getProfileImageUrl())
                .createdAt(DateTimeUtil.formatDateTimeToKorean(post.getCreatedAt()))
                .build();
    }
}
