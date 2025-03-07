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

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/community/posts")
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<CommonResponse<PostDetailResponseDto>> createPost(@Valid @ModelAttribute PostRequestDto requestDto, @RequestParam(value = "images", required = false) List<MultipartFile> multipartFileList, Principal principal) {
        PostDetailResponseDto postDetailResponseDto = postService.createPost(requestDto, multipartFileList, principal.getName());
        return new ResponseEntity<>(CommonResponse.success("글 등록 완료", postDetailResponseDto), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CommonResponse<Boolean>> deletePost(@PathVariable("id") Long id, Principal principal) {
        postService.deletePost(id, principal.getName());
        return new ResponseEntity<>(CommonResponse.success("글 삭제 완료", true), HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommonResponse<PostDetailResponseDto>> updatePost(@PathVariable("id") Long id, @Valid @ModelAttribute PostEditRequestDto requestDto, @RequestParam(value = "newImages", required = false) List<MultipartFile> multipartFileList, Principal principal) {
        PostDetailResponseDto postDetailResponseDto = postService.updatePost(id, requestDto, multipartFileList, principal.getName());
        return new ResponseEntity<>(CommonResponse.success("글 수정 완료", postDetailResponseDto), HttpStatus.OK);
    }

    @GetMapping("/list/{categoryType}/{pageNum}")
    public ResponseEntity<CommonResponse<PostListResponseDto>> getAllPosts(@PathVariable("categoryType") String categoryType, @PathVariable("pageNum") Integer pageNum) {
        PostListResponseDto postListResponseDto = postService.getAllPosts(categoryType, pageNum);
        return new ResponseEntity<>(CommonResponse.success(null, postListResponseDto), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommonResponse<PostDetailResponseDto>> getPostById(@PathVariable("id") Long id, Principal principal) {

        String email = null; // email 초기화
        if (principal != null) { // principal 이 null 이 아닌 경우 (로그인 사용자)
            email = principal.getName(); // principal.getName() 으로 email 획득
        }
        PostDetailResponseDto responseDto = postService.getPostById(id, email); // email 파라미터 (optional) 전달
        postService.addViewCnt(id);
        return new ResponseEntity<>(CommonResponse.success(null, responseDto), HttpStatus.OK);
    }

    @GetMapping("/user/{pageNum}") //
    public ResponseEntity<CommonResponse<PostListResponseDto>> getPostsByUserId(
            @PathVariable("pageNum") Integer pageNum,
            Principal principal) {
        PostListResponseDto responseDtos = postService.getPostsByMemberId(principal.getName(), pageNum);
        return new ResponseEntity<>(CommonResponse.success(null, responseDtos), HttpStatus.OK);
    }
}