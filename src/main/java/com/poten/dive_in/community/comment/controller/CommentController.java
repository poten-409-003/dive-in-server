package com.poten.dive_in.community.comment.controller;

import com.poten.dive_in.common.dto.CommonResponse;
import com.poten.dive_in.community.comment.dto.CommentRequestDTO;
import com.poten.dive_in.community.comment.dto.CommentResponseDTO;
import com.poten.dive_in.community.comment.service.CommentService;
import com.poten.dive_in.community.post.dto.PostListResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/community/comments")
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<CommonResponse<CommentResponseDTO>> createComment(@RequestBody @Valid CommentRequestDTO requestDTO, Principal principal) {
        CommentResponseDTO responseDTO = commentService.createComment(requestDTO, principal.getName());
        return new ResponseEntity<>(CommonResponse.success("댓글 작성 완료", responseDTO), HttpStatus.CREATED);
    }

    @PostMapping("/{id}/reply")
    public ResponseEntity<CommonResponse<CommentResponseDTO>> createReply(@PathVariable("id") Long id, @RequestBody @Valid CommentRequestDTO requestDTO, Principal principal) {
        CommentResponseDTO responseDTO = commentService.createReply(id, requestDTO, principal.getName());
        return new ResponseEntity<>(CommonResponse.success("대댓글 작성 완료", responseDTO), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CommonResponse<Boolean>> deleteComment(@PathVariable("id") Long id, Principal principal) {
        commentService.deleteComment(id, principal.getName());
        return new ResponseEntity<>(CommonResponse.success("댓글 삭제 완료", true), HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommonResponse<CommentResponseDTO>> updateComment(
            @PathVariable("id") Long id,
            @RequestBody @Valid CommentRequestDTO requestDTO, Principal principal) {
        CommentResponseDTO responseDTO = commentService.updateComment(id, requestDTO, principal.getName());
        return new ResponseEntity<>(CommonResponse.success("댓글 수정 완료", responseDTO), HttpStatus.OK);
    }

    @GetMapping("/user/{pageNum}")
    public ResponseEntity<CommonResponse<PostListResponseDto>> getPostsAboutCommentsByMemberId(@PathVariable("pageNum") Integer pageNum, Principal principal) {
        PostListResponseDto postResponseDtos = commentService.getPostsAboutCommentByMemberId(principal.getName(), pageNum);
        return new ResponseEntity<>(CommonResponse.success(null, postResponseDtos), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommonResponse<List<CommentResponseDTO>>> getCommentById(@PathVariable("id") Long id, Principal principal) {
        Long memberId = null;
        if (principal != null) {
            memberId = commentService.getMemberIdByEmail(principal.getName());
        }
        List<CommentResponseDTO> responseDTOs = commentService.getCommentById(id, memberId);
        return new ResponseEntity<>(CommonResponse.success(null, responseDTOs), HttpStatus.OK);
    }
}