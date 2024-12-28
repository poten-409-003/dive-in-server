package com.poten.dive_in.community.post.repository;

import com.poten.dive_in.community.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;


public interface PostRepositoryCustom {
    Optional<Post> findByIdWithMember(Long id);

    Optional<Post> findByIdWithDetail(Long id);

    Page<Post> findActivePosts(Pageable pageable);

    List<Post> findPopularPosts(Pageable pageable);

    Page<Post> findByCategoryCodeCd(String categoryType, Pageable pageable);

    Page<Post> findPostsByMemberId(Pageable pageable, Long memberId);

    Page<Post> searchPosts(String query, Pageable pageable);

}
