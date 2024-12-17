package com.poten.dive_in.community.comment.controller;

import com.poten.dive_in.common.dto.CommonResponse;
import com.poten.dive_in.community.comment.dto.CommentRequestDTO;
import com.poten.dive_in.community.comment.dto.CommentResponseDTO;
import com.poten.dive_in.community.comment.entity.Comment;
import com.poten.dive_in.community.comment.service.CommentService;
import com.poten.dive_in.community.post.entity.Post;
import com.poten.dive_in.auth.entity.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/community/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    // 댓글 생성
    @PostMapping
    public ResponseEntity<CommonResponse<CommentResponseDTO>> createComment(@RequestBody CommentRequestDTO requestDTO) {
        Comment comment = Comment.builder()
                .post(new Post(requestDTO.getPostId())) // 댓글이 달릴 게시글의 ID를 기반으로 Post 객체 생성
                .member(new Member(requestDTO.getMemberId())) // 댓글 작성자의 ID를 기반으로 Member 객체 생성
                .content(requestDTO.getContent()) // 댓글 내용 설정
                .cmntClass(0) // 댓글로 설정
                .build();

        // 댓글 저장
        Comment createdComment = commentService.createComment(comment);

        // 댓글 응답 DTO 생성
        CommentResponseDTO responseDTO = CommentResponseDTO.builder()
                .cmmtId(createdComment.getId()) // 생성된 댓글 ID
                .content(createdComment.getContent()) // 댓글 내용
                .writer(createdComment.getMember().getNickname()) // 작성자 닉네임
                .writerProfile(createdComment.getMember().getProfileImageUrl()) // 작성자 프로필 이미지 URL
                .createdAt(createdComment.getCreatedAt().toString()) // 생성일
                .build();

        return new ResponseEntity<>(CommonResponse.success("Comment created successfully", responseDTO), HttpStatus.CREATED);
    }

    // 대댓글 등록
    @PostMapping("/{id}/reply")
    public ResponseEntity<CommonResponse<CommentResponseDTO>> createReply(@PathVariable Long id, @RequestBody CommentRequestDTO requestDTO) {
        Comment reply = Comment.builder()
                .member(new Member(requestDTO.getMemberId())) // 대댓글 작성자의 ID
                .content(requestDTO.getContent()) // 대댓글 내용
                .cmntClass(1) // 대댓글로 설정
                .build();

        // 대댓글 저장
        Comment createdReply = commentService.createReply(id, reply);

        // 대댓글 응답 DTO 생성
        CommentResponseDTO responseDTO = CommentResponseDTO.builder()
                .cmmtId(createdReply.getId()) // 생성된 대댓글 ID
                .content(createdReply.getContent()) // 대댓글 내용
                .writer(createdReply.getMember().getNickname()) // 작성자 닉네임
                .writerProfile(createdReply.getMember().getProfileImageUrl()) // 작성자 프로필 이미지 URL
                .createdAt(createdReply.getCreatedAt().toString()) // 생성일
                .build();

        return new ResponseEntity<>(CommonResponse.success("Reply created successfully", responseDTO), HttpStatus.CREATED);
    }

    // 댓글 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<CommonResponse<Boolean>> deleteComment(@PathVariable Long id, @RequestParam Long memberId) {
        commentService.deleteComment(id, memberId);
        return new ResponseEntity<>(CommonResponse.success("Comment deleted successfully", true), HttpStatus.NO_CONTENT);
    }

    // 댓글 수정
    @PutMapping("/{id}")
    public ResponseEntity<CommonResponse<CommentResponseDTO>> updateComment(
            @PathVariable Long id,
            @RequestBody CommentRequestDTO requestDTO) {

        Comment updatedComment = Comment.builder()
                .content(requestDTO.getContent()) // 수정된 댓글 내용
                .build();

        Comment comment = commentService.updateComment(id, updatedComment);
        CommentResponseDTO responseDTO = CommentResponseDTO.builder()
                .cmmtId(comment.getId()) // 수정된 댓글 ID
                .content(comment.getContent()) // 수정된 댓글 내용
                .writer(comment.getMember().getNickname()) // 작성자 닉네임
                .writerProfile(comment.getMember().getProfileImageUrl()) // 작성자 프로필 이미지 URL
                .createdAt(comment.getCreatedAt().toString()) // 생성일
                .build();

        return new ResponseEntity<>(CommonResponse.success("Comment updated successfully", responseDTO), HttpStatus.OK);
    }

    // 특정 게시글의 댓글 조회
    @GetMapping("/post/{postId}")
    public ResponseEntity<CommonResponse<List<CommentResponseDTO>>> getCommentsByPostId(@PathVariable Long postId) {
        List<Comment> comments = commentService.getCommentsByPostId(postId);
        List<CommentResponseDTO> responseDTOs = comments.stream()
                .map(comment -> CommentResponseDTO.builder()
                        .cmmtId(comment.getId()) // 댓글 ID
                        .content(comment.getContent()) // 댓글 내용
                        .writer(comment.getMember().getNickname()) // 작성자 닉네임
                        .writerProfile(comment.getMember().getProfileImageUrl()) // 작성자 프로필 이미지 URL
                        .createdAt(comment.getCreatedAt().toString()) // 생성일
                        .build())
                .collect(Collectors.toList());
        return new ResponseEntity<>(CommonResponse.success("Comments retrieved successfully", responseDTOs), HttpStatus.OK);
    }

    // 특정 사용자의 댓글 조회
    @GetMapping("/user/{userId}")
    public ResponseEntity<CommonResponse<List<CommentResponseDTO>>> getCommentsByUserId(@PathVariable Long userId) {
        List<Comment> comments = commentService.getCommentsByUserId(userId);
        List<CommentResponseDTO> responseDTOs = comments.stream()
                .map(comment -> CommentResponseDTO.builder()
                        .cmmtId(comment.getId()) // 댓글 ID
                        .content(comment.getContent()) // 댓글 내용
                        .writer(comment.getMember().getNickname()) // 작성자 닉네임
                        .writerProfile(comment.getMember().getProfileImageUrl()) // 작성자 프로필 이미지 URL
                        .createdAt(comment.getCreatedAt().toString()) // 생성일
                        .build())
                .collect(Collectors.toList());
        return new ResponseEntity<>(CommonResponse.success("User comments retrieved successfully", responseDTOs), HttpStatus.OK);
    }

    // 특정 댓글 조회
    @GetMapping("/{id}")
    public ResponseEntity<CommonResponse<CommentResponseDTO>> getCommentById(@PathVariable Long id) {
        Comment comment = commentService.getCommentById(id); // 댓글 ID로 댓글 조회
        CommentResponseDTO responseDTO = CommentResponseDTO.builder()
                .cmmtId(comment.getId()) // 댓글 ID
                .content(comment.getContent()) // 댓글 내용
                .writer(comment.getMember().getNickname()) // 작성자 닉네임
                .writerProfile(comment.getMember().getProfileImageUrl()) // 작성자 프로필 이미지 URL
                .createdAt(comment.getCreatedAt().toString()) // 생성일
                .build();

        return new ResponseEntity<>(CommonResponse.success("Comment retrieved successfully", responseDTO), HttpStatus.OK);
    }

    // 특정 게시글에 대한 댓글 수 조회
    @GetMapping("/count/{postId}")
    public ResponseEntity<CommonResponse<Integer>> getCommentCountByPostId(@PathVariable Long postId) {
        int count = commentService.getCommentCountByPostId(postId); // 댓글 수 조회
        return new ResponseEntity<>(CommonResponse.success("Comment count retrieved successfully", count), HttpStatus.OK);
    }
}
