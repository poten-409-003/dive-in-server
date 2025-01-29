package com.poten.dive_in.community.post.controller;

import com.poten.dive_in.common.dto.CommonResponse;
import com.poten.dive_in.community.post.dto.*;
import com.poten.dive_in.community.post.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/community/posts")
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<CommonResponse<PostDetailResponseDto>> createPost(@Valid PostRequestDto requestDto, @RequestParam(value = "images", required = false) List<MultipartFile> multipartFileList) {
        PostDetailResponseDto postDetailResponseDto = postService.createPost(requestDto, multipartFileList);
        return new ResponseEntity<>(CommonResponse.success("글 등록 완료", postDetailResponseDto), HttpStatus.CREATED); //201
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CommonResponse<Boolean>> deletePost(@PathVariable("id") Long id, @RequestParam("memberId") Long memberId) {
        postService.deletePost(id, memberId);
        return new ResponseEntity<>(CommonResponse.success("글 삭제 완료", true), HttpStatus.NO_CONTENT); //204
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommonResponse<PostDetailResponseDto>> updatePost(@PathVariable("id") Long id, @Valid @ModelAttribute PostEditRequestDto requestDto, @RequestParam(value = "newImages", required = false) List<MultipartFile> multipartFileList) {
        PostDetailResponseDto postDetailResponseDto = postService.updatePost(id, requestDto, multipartFileList);
        return new ResponseEntity<>(CommonResponse.success("글 수정 완료", postDetailResponseDto), HttpStatus.OK); //200
    }

    @GetMapping("/list/{categoryType}/{pageNum}")
    public ResponseEntity<CommonResponse<PostListResponseDto>> getAllPosts(@PathVariable("categoryType") String categoryType, @PathVariable("pageNum") Integer pageNum) {
        PostListResponseDto postListResponseDto = postService.getAllPosts(categoryType, pageNum);
        return new ResponseEntity<>(CommonResponse.success(null, postListResponseDto), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommonResponse<PostDetailResponseDto>> getPostById(@PathVariable("id") Long id) {
        PostDetailResponseDto responseDto = postService.getPostById(id);
        return new ResponseEntity<>(CommonResponse.success(null, responseDto), HttpStatus.OK);
    }

    @GetMapping("/user/{memberId}/{pageNum}")
    public ResponseEntity<CommonResponse<PostListResponseDto>> getPostsByUserId(@PathVariable("memberId") Long memberId, @PathVariable("pageNum") Integer pageNum) {
        PostListResponseDto responseDtos = postService.getPostsByMemberId(memberId, pageNum);
        return new ResponseEntity<>(CommonResponse.success(null, responseDtos), HttpStatus.OK);
    }

//    @GetMapping("/search/{pageNum}")
//    public ResponseEntity<CommonResponse<List<PostListResponseDto>>> searchPosts(@RequestParam String query, @PathVariable("pageNum") Integer pageNum) {
//        List<PostListResponseDto> responseDtos = postService.searchPosts(query, pageNum);
//        return new ResponseEntity<>(CommonResponse.success(null, responseDtos), HttpStatus.OK);
//    }

}
