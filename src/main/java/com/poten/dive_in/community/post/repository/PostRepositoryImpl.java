package com.poten.dive_in.community.post.repository;

import com.poten.dive_in.community.post.entity.Post;

import java.util.List;

public interface PostRepositoryImpl {
    List<Post> findActivePosts();

    List<Post> findPopularPosts();

    List<Post> findPostsByMemberId(Long memberId);
}
