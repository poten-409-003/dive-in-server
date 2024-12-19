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
@RequestMapping("/community/comments")
@RequiredArgsConstructor
public class CommentLikeController {
    private final CommentLikeService commentLikeService;

    @PostMapping("/{id}/like")
    public ResponseEntity<CommonResponse<Boolean>> likePost(@PathVariable("id") Long id, @RequestParam Long memberId) {
        CommentLike commentLike = commentLikeService.likeComment(id, memberId);
        return new ResponseEntity<>(CommonResponse.success(null, true), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}/like")
    public ResponseEntity<CommonResponse<Boolean>> unlikePost(@PathVariable("id") Long id, @RequestParam Long memberId) {
        commentLikeService.unlikeComment(id, memberId);
        return new ResponseEntity<>(CommonResponse.success(null, true), HttpStatus.OK);
    }
}
