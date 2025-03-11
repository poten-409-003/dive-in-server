package com.poten.dive_in.community.comment.controller;

import com.poten.dive_in.common.dto.CommonResponse;
import com.poten.dive_in.community.comment.service.CommentLikeService;
import com.poten.dive_in.community.post.dto.LikeResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/community/comments")
@RequiredArgsConstructor
public class CommentLikeController {
    private final CommentLikeService commentLikeService;

    @PostMapping("/{id}/like")
    public ResponseEntity<CommonResponse<LikeResponseDto>> likePost(@PathVariable("id") Long id, Principal principal) {
        LikeResponseDto likeResponseDto = commentLikeService.likeComment(id, principal.getName());
        return new ResponseEntity<>(CommonResponse.success(null, likeResponseDto), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}/like")
    public ResponseEntity<CommonResponse<LikeResponseDto>> unlikePost(@PathVariable("id") Long id, Principal principal) {
        LikeResponseDto likeResponseDto = commentLikeService.unlikeComment(id, principal.getName());
        return new ResponseEntity<>(CommonResponse.success(null, likeResponseDto), HttpStatus.OK);
    }
}
