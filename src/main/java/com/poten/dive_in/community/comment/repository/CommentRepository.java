package com.poten.dive_in.community.comment.repository;

import com.poten.dive_in.community.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long>, CommentRepositoryCustom {
    List<Comment> findCommentsByGroupName(Integer groupName);

    Long countByGroupName(Integer groupName);

}
