package com.poten.dive_in.community.post.controller;

import com.poten.dive_in.common.dto.CommonResponse;
import com.poten.dive_in.community.post.entity.PostLike;
import com.poten.dive_in.community.post.service.PostLikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/community/posts")
public class PostLikeController {

    @Autowired
    private PostLikeService postLikeService;

    // 글 좋아요
    @PostMapping("/{id}/like")
    public ResponseEntity<CommonResponse<PostLike>> likePost(@PathVariable Long id, @RequestParam Long memberId) {
        PostLike postLike = postLikeService.likePost(id, memberId);
        return new ResponseEntity<>(CommonResponse.success("Post liked successfully", postLike), HttpStatus.OK);
    }

    // 글 좋아요 취소
    @DeleteMapping("/{id}/like")
    public ResponseEntity<CommonResponse<Boolean>> unlikePost(@PathVariable Long id, @RequestParam Long memberId) {
        postLikeService.unlikePost(id, memberId);
        return new ResponseEntity<>(CommonResponse.success("Post like removed successfully", true), HttpStatus.OK);
    }
}
