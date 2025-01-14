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

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/community/comments")
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<CommonResponse<CommentResponseDTO>> createComment(@RequestBody @Valid CommentRequestDTO requestDTO) {
        CommentResponseDTO responseDTO = commentService.createComment(requestDTO);
        return new ResponseEntity<>(CommonResponse.success("댓글 작성 완료", responseDTO), HttpStatus.CREATED);
    }

    @PostMapping("/{id}/reply")
    public ResponseEntity<CommonResponse<CommentResponseDTO>> createReply(@PathVariable("id") Long id, @RequestBody @Valid CommentRequestDTO requestDTO) {
        CommentResponseDTO responseDTO = commentService.createReply(id, requestDTO);

        return new ResponseEntity<>(CommonResponse.success("대댓글 작성 완료", responseDTO), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CommonResponse<Boolean>> deleteComment(@PathVariable("id") Long id, @RequestParam("memberId") Long memberId) {
        commentService.deleteComment(id, memberId);
        return new ResponseEntity<>(CommonResponse.success("댓글 삭제 완료", true), HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommonResponse<CommentResponseDTO>> updateComment(
            @PathVariable("id") Long id,
            @RequestBody @Valid CommentRequestDTO requestDTO) {
        CommentResponseDTO responseDTO = commentService.updateComment(id, requestDTO);

        return new ResponseEntity<>(CommonResponse.success("댓글 수정 완료", responseDTO), HttpStatus.OK);
    }

    @GetMapping("/user/{memberId}/{pageNum}")
    public ResponseEntity<CommonResponse<List<PostListResponseDto>>> getPostsAboutCommentsByMemberId(@PathVariable("memberId") Long memberId, @PathVariable("pageNum") Integer pageNum) {
        List<PostListResponseDto> postListResponseDtos = commentService.getPostsAboutCommentByMemberId(memberId, pageNum);
        return new ResponseEntity<>(CommonResponse.success(null, postListResponseDtos), HttpStatus.OK);

    }

    @GetMapping("/{id}/{memberId}")
    public ResponseEntity<CommonResponse<List<CommentResponseDTO>>> getCommentById(@PathVariable("id") Long id, @PathVariable(name = "memberId", required = false) Long memberId) {
        List<CommentResponseDTO> responseDTOs = commentService.getCommentById(id, memberId);
        return new ResponseEntity<>(CommonResponse.success(null, responseDTOs), HttpStatus.OK);
    }


}
