package com.poten.dive_in.community.post.repository;

import com.poten.dive_in.community.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryCustom {
    //Post.detail을 사용하는 메서드
//    @EntityGraph(value = "Post.detail", type = EntityGraph.EntityGraphType.LOAD)
//    Optional<Post> findByIdWithDetail(Long id);
    List<Post> findByTitleContainingOrContentContaining(String title, String content);

}

