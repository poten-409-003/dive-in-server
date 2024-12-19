package com.poten.dive_in.community.comment.repository;

import com.poten.dive_in.community.comment.entity.Comment;
import com.poten.dive_in.community.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface CommentRepositoryCustom {
    Optional<Comment> findByIdWithMember(Long id);

    Optional<Comment> findByIdWithMemberAndPost(Long id);
    Page<Post> findDistinctPostsByMemberId(Long memberId, Pageable pageable);
    List<Comment> findCommentsById(Long id);


}
