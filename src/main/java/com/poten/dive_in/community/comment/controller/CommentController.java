package com.poten.dive_in.community.comment.controller;

import com.poten.dive_in.community.comment.dto.CommentRequestDTO;
import com.poten.dive_in.community.comment.entity.Comment;
import com.poten.dive_in.community.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/community/comments")
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<Boolean> createComment(@RequestBody CommentRequestDTO requestDTO) {
        Comment comment = new Comment();
//        comment.setContent(requestDTO.getContent());
//        comment.setPost(new Post(requestDTO.getPostId())); // 예시로 Post 객체 설정
//        comment.setMember(new Member(requestDTO.getMemberId())); // 예시로 Member 객체 설정

        commentService.createComment(comment);
        return new ResponseEntity<>(true, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteComment(@PathVariable Long id, @RequestParam Long memberId) {
        commentService.deleteComment(id, memberId);
        return new ResponseEntity<>(true, HttpStatus.NO_CONTENT);
    }

//    @PutMapping("/{id}")
//    public ResponseEntity<CommentResponseDTO> updateComment(
//            @PathVariable Long id,
//            @RequestBody CommentRequestDTO requestDTO) {
//
//        Comment updatedComment = new Comment();
////        updatedComment.setContent(requestDTO.getContent());
//
//        Comment comment = commentService.updateComment(id, updatedComment);
//        CommentResponseDTO responseDTO = CommentResponseDTO.builder()
//                .cm
}