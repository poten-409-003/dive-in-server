package com.poten.dive_in.community.post.repository;

import com.poten.dive_in.community.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findByIsActive(String isActive);


    List<Post> findPopularPosts();

    List<Post> findByMemberId(Long memberId);
}

