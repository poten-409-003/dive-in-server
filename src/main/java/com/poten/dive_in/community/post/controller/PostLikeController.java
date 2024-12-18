package com.poten.dive_in.community.post.controller;

import com.poten.dive_in.common.dto.CommonResponse;
import com.poten.dive_in.community.post.entity.PostLike;
import com.poten.dive_in.community.post.service.PostLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/community/posts")
@RequiredArgsConstructor
public class PostLikeController {

    private final PostLikeService postLikeService;

    // 글 좋아요
    @PostMapping("/{id}/like")
    public ResponseEntity<CommonResponse<Boolean>> likePost(@PathVariable Long id, @RequestParam Long memberId) {
        PostLike postLike = postLikeService.likePost(id, memberId);
        return new ResponseEntity<>(CommonResponse.success(null, true), HttpStatus.OK);
    }

    // 글 좋아요 취소
    @DeleteMapping("/{id}/like")
    public ResponseEntity<CommonResponse<Boolean>> unlikePost(@PathVariable Long id, @RequestParam Long memberId) {
        postLikeService.unlikePost(id, memberId);
        return new ResponseEntity<>(CommonResponse.success(null, true), HttpStatus.OK);
    }
}
