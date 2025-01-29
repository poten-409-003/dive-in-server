package com.poten.dive_in.community.post.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class PostListResponseDto {
    private List<PostResponseDto> posts;
    private Long totalPosts;
    private Boolean hasMore;

    public static PostListResponseDto toPostListResponseDto(List<PostResponseDto> posts, Long totalPosts, Boolean hasMore) {
        return PostListResponseDto.builder()
                .posts(posts)
                .totalPosts(totalPosts)
                .hasMore(hasMore)
                .build();
    }
}
