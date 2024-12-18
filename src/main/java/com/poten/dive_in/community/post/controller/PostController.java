package com.poten.dive_in.community.post.controller;

import com.poten.dive_in.cmmncode.service.CmmnCdService;
import com.poten.dive_in.common.dto.CommonResponse;
import com.poten.dive_in.community.post.dto.PostListResponseDto;
import com.poten.dive_in.community.post.dto.PostRequestDto;
import com.poten.dive_in.community.post.dto.PostDetailResponseDto;
import com.poten.dive_in.community.post.entity.Post;
import com.poten.dive_in.community.post.entity.PostImage;
import com.poten.dive_in.community.post.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/community/posts")
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<CommonResponse<PostDetailResponseDto>> createPost(@RequestBody @Valid PostRequestDto requestDTO) {
        PostDetailResponseDto postDetailResponseDto = postService.createPost(requestDTO);
        return new ResponseEntity<>(CommonResponse.success("글 등록 완료", postDetailResponseDto), HttpStatus.CREATED); //201
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CommonResponse<Boolean>> deletePost(@PathVariable Long id, @RequestParam Long memberId) {
        postService.deletePost(id, memberId);
        return new ResponseEntity<>(CommonResponse.success("글 삭제 완료", true), HttpStatus.NO_CONTENT); //204
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommonResponse<PostDetailResponseDto>> updatePost(@PathVariable Long id, @RequestBody @Valid PostRequestDto requestDTO) {
        Post post = postService.updatePost(id, requestDTO);
        return new ResponseEntity<>(CommonResponse.success("글 수정 완료", PostDetailResponseDto.ofEntity(post)), HttpStatus.OK); //200
    }

    @GetMapping("/list/{categoryType}/{pageNum}")
    public ResponseEntity<CommonResponse<List<PostListResponseDto>>> getAllPosts(@PathVariable String categoryType, @PathVariable Integer pageNum) {
        List<PostListResponseDto> responseDTOs = postService.getAllPosts(categoryType, pageNum);
        return new ResponseEntity<>(CommonResponse.success(null, responseDTOs), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommonResponse<PostDetailResponseDto>> getPostById(@PathVariable Long id) {
        PostDetailResponseDto responseDto = postService.getPostById(id);
        return new ResponseEntity<>(CommonResponse.success(null, responseDto), HttpStatus.OK);
    }

    @GetMapping("/user/{memberId}/{pageNum}")
    public ResponseEntity<CommonResponse<List<PostListResponseDto>>> getPostsByUserId(@PathVariable Long memberId, @PathVariable Integer pageNum) {
        List<PostListResponseDto> responseDtos = postService.getPostsByUserId(memberId, pageNum);
        return new ResponseEntity<>(CommonResponse.success(null, responseDtos), HttpStatus.OK);
    }


}
