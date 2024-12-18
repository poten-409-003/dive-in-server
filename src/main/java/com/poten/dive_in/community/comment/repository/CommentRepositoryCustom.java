package com.poten.dive_in.community.comment.repository;

import com.poten.dive_in.community.comment.entity.Comment;
import com.poten.dive_in.community.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface CommentRepositoryCustom {
    Optional<Comment> findByIdWithMember(Long id);
    Page<Post> findDistinctPostsByMemberId(Long memberId, Pageable pageable);
}
