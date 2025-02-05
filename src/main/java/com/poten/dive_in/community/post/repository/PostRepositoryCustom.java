package com.poten.dive_in.community.post.repository;

import com.poten.dive_in.community.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;


public interface PostRepositoryCustom {
    Optional<Post> findByIdWithMember(Long id);

    Optional<Post> findByIdWithDetail(Long id);

    Page<Post> findActivePosts(Pageable pageable);

    List<Post> findPopularPosts(LocalDateTime conditionDate);

    Set<Long> findPopularPostIds(LocalDateTime oneMonthAgo);

    Page<Post> findByCategoryCodeCd(String categoryType, Pageable pageable);

    Page<Post> findPostsByMemberId(Pageable pageable, Long memberId);

    Long countPostsByMemberId(Long memberId);

    Long countActivePosts();

    Long countPostsByCategoryCode(String categoryType);
//    Page<Post> searchPosts(String query, Pageable pageable);

}
