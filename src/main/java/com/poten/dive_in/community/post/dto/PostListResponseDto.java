package com.poten.dive_in.community.post.dto;

import com.poten.dive_in.common.service.DateTimeUtil;
import com.poten.dive_in.community.post.entity.Post;
import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
public class PostListResponseDto {
    private Long postId;
    private String title;
    private String content;
    private PostImageDto image;
    private Integer likesCnt;
    private Integer cmmtCnt;
    private String writer;
    private String writerProfile;
    private String createdAt;

    public static PostListResponseDto ofEntity(Post post){
        PostImageDto postImageDto = (post.getImages() != null) ?
                PostImageDto.ofEntity(post.getImages().stream().toList().get(0)) : null;

        return PostListResponseDto.builder()
                .postId(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .image(postImageDto)
                .likesCnt(post.getLikeCount())
                .cmmtCnt(post.getCommentCount())
                .writer(post.getMember().getNickname())
                .writerProfile(post.getMember().getProfileImageUrl())
                .createdAt(DateTimeUtil.formatDateTimeToKorean(post.getCreatedAt()))
                .build();
    }
}
