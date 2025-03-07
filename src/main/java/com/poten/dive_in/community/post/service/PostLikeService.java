package com.poten.dive_in.community.post.service;

import com.poten.dive_in.auth.entity.Member;
import com.poten.dive_in.auth.repository.MemberRepository;
import com.poten.dive_in.community.post.dto.LikeResponseDto;
import com.poten.dive_in.community.post.entity.Post;
import com.poten.dive_in.community.post.entity.PostLike;
import com.poten.dive_in.community.post.repository.PostLikeRepository;
import com.poten.dive_in.community.post.repository.PostRepository;
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
    public LikeResponseDto likePost(Long postId, String email) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("글이 존재하지 않습니다."));
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 회원입니다."));


        if (postLikeRepository.existsByPostIdAndMemberId(postId, member.getId())) {
            throw new IllegalArgumentException("이미 좋아요를 눌렀습니다.");
        }
        PostLike postLike = PostLike.builder()
                .post(post)
                .member(member)
                .build();

        postLikeRepository.save(postLike);

        post.adjustLikeCount(1);
        postRepository.save(post);

        return LikeResponseDto.builder()
                .likeCnt(post.getLikeCount())
                .build();
    }

    @Transactional
    public LikeResponseDto unlikePost(Long postId, String email) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("글이 존재하지 않습니다."));

        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 회원입니다."));

        PostLike postLike = postLikeRepository.findByPostIdAndMemberId(postId, member.getId())
                .orElseThrow(() -> new IllegalArgumentException("좋아요를 누른 이력이 없습니다."));

        postLikeRepository.delete(postLike);

        // 좋아요 수 감소
        post.adjustLikeCount(-1);
        postRepository.save(post);

        return LikeResponseDto.builder()
                .likeCnt(post.getLikeCount())
                .build();
    }
}
