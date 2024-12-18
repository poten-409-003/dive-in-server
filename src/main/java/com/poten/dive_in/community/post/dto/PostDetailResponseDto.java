package com.poten.dive_in.community.post.dto;

import com.poten.dive_in.common.service.DateTimeUtil;
import com.poten.dive_in.community.comment.dto.CommentResponseDTO;
import com.poten.dive_in.community.post.entity.Post;
import com.poten.dive_in.community.post.entity.PostLike;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class PostDetailResponseDto {
    private Long postId;
    private String title;
    private String content;
    private List<PostImageDto> images; // 이미지 URL 리스트
    private Integer likesCnt; // 좋아요 수
    private Integer cmmtCnt; // 댓글 수
    private String writer; // 작성자 이름
    private String writerProfile; // 작성자 프로필 이미지 URL
    private String createdAt;
    private List<CommentResponseDTO> commentList;
    private Boolean isLiked;

    public static PostDetailResponseDto ofEntity(Post post){

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

        return PostDetailResponseDto.builder()
                .postId(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .images(postImageDtoList)
                .likesCnt(post.getLikeCount())
                .cmmtCnt(post.getCommentCount())
                .writer(post.getMember().getNickname())
                .writerProfile(post.getMember().getProfileImageUrl())
                .createdAt(DateTimeUtil.formatDateTimeToKorean(post.getCreatedAt()))
                .commentList(commentResponseDTOList)
                .build();
    }
}
