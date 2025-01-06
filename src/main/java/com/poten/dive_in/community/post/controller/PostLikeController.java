package com.poten.dive_in.community.post.controller;

import com.poten.dive_in.common.dto.CommonResponse;
import com.poten.dive_in.community.post.dto.LikeResponseDto;
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

    @PostMapping("/{id}/like")
    public ResponseEntity<CommonResponse<LikeResponseDto>> likePost(@PathVariable("id") Long id, @RequestParam("memberId") Long memberId) {
        LikeResponseDto likeResponseDto = postLikeService.likePost(id, memberId);
        return new ResponseEntity<>(CommonResponse.success(null, likeResponseDto), HttpStatus.OK);
    }

    @DeleteMapping("/{id}/like")
    public ResponseEntity<CommonResponse<LikeResponseDto>> unlikePost(@PathVariable("id") Long id, @RequestParam("memberId") Long memberId) {
        LikeResponseDto likeResponseDto = postLikeService.unlikePost(id, memberId);
        return new ResponseEntity<>(CommonResponse.success(null, likeResponseDto), HttpStatus.OK);
    }
}
