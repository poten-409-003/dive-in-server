package com.poten.dive_in.community.comment.service;

import com.poten.dive_in.community.comment.entity.Comment;
import com.poten.dive_in.community.comment.repository.CommentRepository;
import com.poten.dive_in.community.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostService postService; // PostService가 필요합니다.

    @Transactional
    public Comment createComment(Comment comment) {
        return commentRepository.save(comment);
    }

    @Transactional
    public Comment createReply(Long parentId, Comment reply) {
        // 부모 댓글 조회
        Comment parentComment = commentRepository.findById(parentId)
                .orElseThrow(() -> new IllegalArgumentException("Parent comment not found"));

        // 대댓글을 생성
        Comment createdReply = Comment.builder()
                .post(parentComment.getPost()) // 부모 댓글의 게시글 설정
                .member(reply.getMember()) // 대댓글 작성자 설정
                .content(reply.getContent()) // 대댓글 내용 설정
                .cmntClass(1) // 대댓글로 설정
                .groupName(parentComment.getGroupName()) // 부모 댓글의 그룹명 설정
                .commentSequence(parentComment.getCommentSequence() + 1) // 댓글 순서 설정
                .build();

        return commentRepository.save(createdReply);
    }

    @Transactional
    public void deleteComment(Long commentId, Long memberId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Comment not found"));

        // 작성자 확인
        if (!comment.getMember().getId().equals(memberId)) {
            throw new IllegalArgumentException("You can only delete your own comments");
        }

        commentRepository.delete(comment);
    }

    @Transactional
    public Comment updateComment(Long commentId, Comment updatedComment) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Comment not found"));

        comment.assignContent(updatedComment.getContent());

        return commentRepository.save(comment);
    }

    public List<Comment> getCommentsByPostId(Long postId) {
        return commentRepository.findByPostId(postId);
    }

    public List<Comment> getCommentsByUserId(Long userId) {
        return commentRepository.findByMemberId(userId);
    }

    public Comment getCommentById(Long id) {
        return commentRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Comment not found"));
    }

    public int getCommentCountByPostId(Long postId) {
        return commentRepository.countByPostId(postId);
    }
}
