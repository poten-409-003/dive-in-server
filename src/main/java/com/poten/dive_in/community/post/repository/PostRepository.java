package com.poten.dive_in.community.post.repository;

import com.poten.dive_in.community.post.entity.Post;
import com.poten.dive_in.lesson.entity.SwimClass;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryImpl {
    List<Post> findByCategoryCodeCd(String categoryCode);
    // member를 로드하는 메서드
    @EntityGraph(attributePaths = {"member"}, type = EntityGraph.EntityGraphType.LOAD)
    Optional<Post> findByIdWithMember(Long id);

    // "Post.detail"을 사용하는 메서드
    @EntityGraph(value = "Post.detail", type = EntityGraph.EntityGraphType.LOAD)
    Optional<Post> findByIdWithDetail(Long id);
}

