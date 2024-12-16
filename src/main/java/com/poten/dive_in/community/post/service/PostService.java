package com.poten.dive_in.community.post.service;


import com.poten.dive_in.community.post.entity.Post;
import com.poten.dive_in.community.post.repository.PostRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    @Transactional
    public Post createPost(Post post, List<MultipartFile> images, boolean isContainsUrl) {
        // 이미지 처리 로직 추가 필요
        // 예: images를 PostImage로 변환하여 post에 설정
        return postRepository.save(post);
    }

    @Transactional
    public void deletePost(Long id, Long memberId) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));
        // 작성자 확인
        if (!post.getMember().getId().equals(memberId)) {
            throw new IllegalArgumentException("You can only delete your own posts");
        }
        postRepository.delete(post);
    }

    @Transactional
    public Post updatePost(Long id, Post updatedPost, List<MultipartFile> images, boolean isContainsUrl) {
        Post existingPost = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));

        // 새로운 Post 객체 생성
        Post newPost = Post.builder()
                .id(existingPost.getId())
                .member(existingPost.getMember())
                .title(updatedPost.getTitle())
                .content(updatedPost.getContent())
                .categoryCode(updatedPost.getCategoryCode())
                .commentCount(existingPost.getCommentCount())
                .likeCount(existingPost.getLikeCount())
                .viewCount(existingPost.getViewCount())
                .isActive(existingPost.getIsActive())
                .images(existingPost.getImages()) // 이미지 처리 필요
                .build();

        return postRepository.save(newPost);
    }

    public List<Post> getAllPosts(String categoryType) {
        if ("none".equals(categoryType)) {
            return postRepository.findActivePosts();
        } else if ("popular".equals(categoryType)) {
            return postRepository.findPopularPosts();
        } else {
            return postRepository.findByCategoryCodeCd(categoryType);
        }
    }

    public Post getPostById(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));
    }

    public List<Post> getPostsByUserId(Long userId) {
        return postRepository.findPostsByMemberId(userId);
    }
}


