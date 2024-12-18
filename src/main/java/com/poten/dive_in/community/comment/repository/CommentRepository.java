package com.poten.dive_in.community.comment.repository;

import com.poten.dive_in.community.comment.entity.Comment;
import com.poten.dive_in.community.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long>, CommentRepositoryCustom {
    @EntityGraph(attributePaths = {"post"}, type = EntityGraph.EntityGraphType.LOAD)
    Page<Post> findPostDistinctByMemberId(Long memberId, Pageable pageable);

    List<Comment> findCommentsByGroupName(Integer groupName);
    Long countByGroupName(Integer groupName);
}
