package com.poten.dive_in.community.post.controller;

import com.poten.dive_in.cmmncode.entity.CommonCode;
import com.poten.dive_in.cmmncode.service.CmmnCdService;
import com.poten.dive_in.common.dto.CommonResponse;
import com.poten.dive_in.community.post.dto.PostRequestDTO;
import com.poten.dive_in.community.post.dto.PostResponseDTO;
import com.poten.dive_in.community.post.entity.Post;
import com.poten.dive_in.community.post.entity.PostImage;
import com.poten.dive_in.community.post.entity.PostLike;
import com.poten.dive_in.community.post.service.PostService;
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
    private final CmmnCdService cmmnCdService;

    @PostMapping
    public ResponseEntity<CommonResponse<Boolean>> createPost(@ModelAttribute PostRequestDTO requestDTO) {
        CommonCode commonCode = cmmnCdService.getCommonCode(requestDTO.getCategoryType());

        Post post = Post.builder()
                .title(requestDTO.getTitle())
                .content(requestDTO.getContent())
                .categoryCode(commonCode)
                .build();

        postService.createPost(post, requestDTO.getImages(), requestDTO.getIsContainsUrl());
        return new ResponseEntity<>(CommonResponse.success("글 저장 완료", true), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CommonResponse<Boolean>> deletePost(@PathVariable Long id, @RequestParam Long memberId) {
        postService.deletePost(id, memberId);
        return new ResponseEntity<>(CommonResponse.success("글 삭제 완료", true), HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommonResponse<PostResponseDTO>> updatePost(
            @PathVariable Long id,
            @ModelAttribute PostRequestDTO requestDTO) {

        Post updatedPost = Post.builder()
                .title(requestDTO.getTitle())
                .content(requestDTO.getContent())
                .build();

        Post post = postService.updatePost(id, updatedPost, requestDTO.getImages(), requestDTO.getIsContainsUrl());

        PostResponseDTO responseDTO = PostResponseDTO.builder()
                .postId(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .images(post.getImages().stream().map(PostImage::getImageUrl).collect(Collectors.toList()))
                .likesCnt(post.getLikeCount())
                .cmmtCnt(post.getCommentCount())
                .writer(post.getMember().getNickname())
                .writerProfile(post.getMember().getProfileImageUrl())
                .build();

        return new ResponseEntity<>(CommonResponse.success("글 수정 완료", responseDTO), HttpStatus.OK);
    }

    @GetMapping("/list/{categoryType}")
    public ResponseEntity<CommonResponse<List<PostResponseDTO>>> getAllPosts(@PathVariable String categoryType) {
        List<Post> posts = postService.getAllPosts(categoryType);
        List<PostResponseDTO> responseDTOs = posts.stream()
                .map(post -> PostResponseDTO.builder()
                        .postId(post.getId())
                        .title(post.getTitle())
                        .content(post.getContent())
                        .images(post.getImages().stream().map(PostImage::getImageUrl).collect(Collectors.toList()))
                        .likesCnt(post.getLikeCount())
                        .cmmtCnt(post.getCommentCount())
                        .writer(post.getMember().getNickname())
                        .writerProfile(post.getMember().getProfileImageUrl())
                        .build())
                .collect(Collectors.toList());
        return new ResponseEntity<>(CommonResponse.success("Posts retrieved successfully", responseDTOs), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommonResponse<PostResponseDTO>> getPostById(@PathVariable Long id) {
        Post post = postService.getPostById(id);
        PostResponseDTO responseDTO = PostResponseDTO.builder()
                .postId(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .images(post.getImages().stream().map(PostImage::getImageUrl).collect(Collectors.toList()))
                .likesCnt(post.getLikeCount())
                .cmmtCnt(post.getCommentCount())
                .writer(post.getMember().getNickname())
                .writerProfile(post.getMember().getProfileImageUrl())
                .build();
        return new ResponseEntity<>(CommonResponse.success("Post retrieved successfully", responseDTO), HttpStatus.OK);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<CommonResponse<List<PostResponseDTO>>> getPostsByUserId(@PathVariable Long userId) {
        List<Post> posts = postService.getPostsByUserId(userId);
        List<PostResponseDTO> responseDTOs = posts.stream()
                .map(post -> PostResponseDTO.builder()
                        .postId(post.getId())
                        .title(post.getTitle())
                        .content(post.getContent())
                        .images(post.getImages().stream().map(PostImage::getImageUrl).collect(Collectors.toList()))
                        .likesCnt(post.getLikeCount())
                        .cmmtCnt(post.getCommentCount())
                        .writer(post.getMember().getNickname())
                        .writerProfile(post.getMember().getProfileImageUrl())
                        .build())
                .collect(Collectors.toList());
        return new ResponseEntity<>(CommonResponse.success("User posts retrieved successfully", responseDTOs), HttpStatus.OK);
    }

}
