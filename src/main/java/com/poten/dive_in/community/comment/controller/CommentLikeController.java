package com.poten.dive_in.community.comment.controller;

import com.poten.dive_in.common.dto.CommonResponse;
import com.poten.dive_in.community.comment.entity.Comment;
import com.poten.dive_in.community.comment.entity.CommentLike;
import com.poten.dive_in.community.comment.service.CommentLikeService;
import com.poten.dive_in.community.post.entity.PostLike;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/community/comment")
@RequiredArgsConstructor
public class CommentLikeController {
    private final CommentLikeService commentLikeService;

    // 댓글 좋아요
    @PostMapping("/{id}/like")
    public ResponseEntity<CommonResponse<CommentLike>> likePost(@PathVariable Long id, @RequestParam Long memberId) {
        CommentLike commentLike = commentLikeService.likeComment(id, memberId);
        return new ResponseEntity<>(CommonResponse.success(null, commentLike), HttpStatus.CREATED);
    }

    // 댓글 좋아요 취소
    @DeleteMapping("/{id}/like")
    public ResponseEntity<CommonResponse<Boolean>> unlikePost(@PathVariable Long id, @RequestParam Long memberId) {
        commentLikeService.unlikeComment(id, memberId);
        return new ResponseEntity<>(CommonResponse.success(null, true), HttpStatus.OK);
    }
}
