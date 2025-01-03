package com.poten.dive_in.community.comment.dto;

import com.poten.dive_in.common.service.DateTimeUtil;
import com.poten.dive_in.community.comment.entity.Comment;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CommentResponseDTO {
    private Long cmmtId; // 댓글 ID
    private String content; // 댓글 내용
    private Integer groupName; //댓글의Id를 groupName으로
    private Integer orderNumber; //댓글 순서
    private Integer cmntClass;// 0: 댓글, 1: 대댓글
    private String writer; // 작성자 이름
    private String writerProfile; // 작성자 프로필 이미지 URL
    private Integer likeCnt; // 좋아요 수
    private String createdAt; // 생성 날짜

    public static CommentResponseDTO ofEntity(Comment comment) {
        return CommentResponseDTO.builder()
                .cmmtId(comment.getId())
                .content(comment.getContent())
                .groupName(comment.getGroupName())
                .orderNumber(comment.getOrderNumber())
                .cmntClass(comment.getCmntClass())
                .writer(comment.getMember().getNickname())
                .writerProfile(comment.getMember().getProfileImageUrl())
                .likeCnt(comment.getLikeCount())
                .createdAt(DateTimeUtil.formatDateTimeToKorean(comment.getCreatedAt()))
                .build();

    }
}
