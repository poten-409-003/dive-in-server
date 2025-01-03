package com.poten.dive_in.community.comment.service;

import com.poten.dive_in.auth.entity.Member;
import com.poten.dive_in.auth.repository.MemberRepository;
import com.poten.dive_in.community.comment.entity.Comment;
import com.poten.dive_in.community.comment.entity.CommentLike;
import com.poten.dive_in.community.comment.repository.CommentLikeRepository;
import com.poten.dive_in.community.comment.repository.CommentRepository;
import com.poten.dive_in.community.post.dto.LikeResponseDto;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentLikeService {

    private final CommentRepository commentRepository;
    private final CommentLikeRepository commentLikeRepository;
    private final MemberRepository memberRepository;


    @Transactional
    public LikeResponseDto likeComment(Long commentId, Long memberId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("댓글이 존재하지 않습니다."));

        if (commentLikeRepository.existsByCommentIdAndMemberId(commentId, memberId)) {
            throw new IllegalArgumentException("이미 좋아요를 눌렀습니다.");
        }

        Member member = memberRepository.findById(memberId).orElseThrow(() -> new EntityNotFoundException("해당 회원이 존재하지 않습니다."));

        CommentLike commentLike = CommentLike.builder()
                .comment(comment)
                .member(member)
                .build();

        commentLikeRepository.save(commentLike);

        comment.adjustLikeCount(1);
        commentRepository.save(comment);

        return LikeResponseDto.builder()
                .likeCnt(comment.getLikeCount())
                .build();
    }

    @Transactional
    public LikeResponseDto unlikeComment(Long commentId, Long memberId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("댓글이 존재하지 않습니다."));

        CommentLike commentLike = commentLikeRepository.findByCommentIdAndMemberId(commentId, memberId)
                .orElseThrow(() -> new IllegalArgumentException("좋아요를 누른 이력이 없습니다."));

        commentLikeRepository.delete(commentLike);

        comment.adjustLikeCount(-1);
        commentRepository.save(comment);

        return LikeResponseDto.builder()
                .likeCnt(comment.getLikeCount())
                .build();
    }
}
