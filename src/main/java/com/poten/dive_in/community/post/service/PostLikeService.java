package com.poten.dive_in.community.post.service;

import com.poten.dive_in.auth.entity.Member;
import com.poten.dive_in.auth.repository.MemberRepository;
import com.poten.dive_in.community.post.entity.Post;
import com.poten.dive_in.community.post.entity.PostLike;
import com.poten.dive_in.community.post.repository.PostRepository;
import com.poten.dive_in.community.post.repository.PostLikeRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostLikeService {

    private final PostRepository postRepository;
    private final PostLikeRepository postLikeRepository;
    private final MemberRepository memberRepository;


    @Transactional
    public PostLike likePost(Long postId, Long memberId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));

        // 이미 좋아요가 눌렸는지 확인
        if (postLikeRepository.existsByPostIdAndMemberId(postId, memberId)) {
            throw new IllegalArgumentException("You have already liked this post");
        }

        Member member = memberRepository.findById(memberId).orElseThrow(()-> new EntityNotFoundException("해당 회원이 존재하지 않습니다."));
        PostLike postLike = PostLike.builder()
                .post(post)
                .member(member) // Member 객체는 실제로 데이터베이스에서 조회해야 할 수 있습니다.
                .build();

        postLikeRepository.save(postLike);

        // 좋아요 수 증가
        post.adjustLikeCount(1);
        postRepository.save(post);

        return postLike;
    }

    @Transactional
    public void unlikePost(Long postId, Long memberId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));

        PostLike postLike = postLikeRepository.findByPostIdAndMemberId(postId, memberId)
                .orElseThrow(() -> new IllegalArgumentException("Like not found"));

        postLikeRepository.delete(postLike);

        // 좋아요 수 감소
        post.adjustLikeCount(-1);
        postRepository.save(post);
    }
}
