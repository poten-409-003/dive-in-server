package com.poten.dive_in.community.comment.repository;

import com.poten.dive_in.community.comment.entity.CommentLike;
import com.poten.dive_in.community.post.entity.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {
    boolean existsByCommentIdAndMemberId(Long commentId, Long memberId);
    Optional<CommentLike> findByCommentIdAndMemberId(Long commentId, Long memberId);

}
