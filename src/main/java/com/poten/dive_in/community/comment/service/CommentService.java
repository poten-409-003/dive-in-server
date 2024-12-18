package com.poten.dive_in.community.comment.service;

import com.poten.dive_in.auth.entity.Member;
import com.poten.dive_in.auth.repository.MemberRepository;
import com.poten.dive_in.community.comment.dto.CommentRequestDTO;
import com.poten.dive_in.community.comment.dto.CommentResponseDTO;
import com.poten.dive_in.community.comment.entity.Comment;
import com.poten.dive_in.community.comment.repository.CommentRepository;
import com.poten.dive_in.community.post.dto.PostListResponseDto;
import com.poten.dive_in.community.post.entity.Post;
import com.poten.dive_in.community.post.repository.PostRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;

    @Transactional
    public CommentResponseDTO createComment(CommentRequestDTO requestDTO) {
        Member member = memberRepository.findById(requestDTO.getMemberId()).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 회원입니다."));
        Post post = postRepository.findById(requestDTO.getPostId()).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 글입니다."));

        Comment comment = Comment.builder()
                .post(post)
                .member(member)
                .content(requestDTO.getContent())
                .cmntClass(0) // 댓글로 설정
                .orderNumber(0)
                .build();
        Comment createdComment = commentRepository.save(comment);
        createdComment.assignGroupName(createdComment.getId().intValue());
        Comment updatedComment = commentRepository.save(createdComment);

        return CommentResponseDTO.ofEntity(updatedComment);
    }

    @Transactional
    public CommentResponseDTO createReply(Long parentId, CommentRequestDTO requestDTO) {
        Member member = memberRepository.findById(requestDTO.getMemberId()).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 회원입니다."));
        Post post = postRepository.findById(requestDTO.getPostId()).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 글입니다."));

        // 부모 댓글 조회
        Comment parentComment = commentRepository.findById(parentId)
                .orElseThrow(() -> new EntityNotFoundException("댓글이 존재하지 않습니다."));

        // 대댓글의 그룹 번호와 순서 번호 설정
        Integer groupName = parentComment.getGroupName();
        Integer orderNumber = commentRepository.countByGroupName(groupName).intValue();

        Comment reply = Comment.builder()
                .member(member)
                .post(post)
                .content(requestDTO.getContent())
                .groupName(groupName)
                .orderNumber(orderNumber) // 순서 번호 설정
                .cmntClass(1) // 대댓글로 설정
                .build();

        Comment savedReply = commentRepository.save(reply);

        return CommentResponseDTO.ofEntity(savedReply);
    }

    @Transactional
    public void deleteComment(Long commentId, Long memberId) {
        //댓글 존재 확인
        Comment comment = commentRepository.findByIdWithMember(commentId)
                .orElseThrow(() -> new EntityNotFoundException("댓글이 존재하지 않습니다."));

        // 작성자 확인
        if (!comment.getMember().getId().equals(memberId)) {
            throw new EntityNotFoundException("본인의 댓글만 삭제할 수 있습니다.");
        }

        //대댓글 확인
        List<Comment> replies = commentRepository.findCommentsByGroupName(comment.getGroupName());
        for (Comment reply : replies) {
            commentRepository.delete(reply);
        }
        commentRepository.delete(comment);
    }

    @Transactional
    public CommentResponseDTO updateComment(Long commentId, CommentRequestDTO requestDTO) {
        //댓글 존재 확인
        Comment comment = commentRepository.findByIdWithMember(commentId)
                .orElseThrow(() -> new EntityNotFoundException("댓글이 존재하지 않습니다."));

        // 작성자 확인
        if (!comment.getMember().getId().equals(requestDTO.getMemberId())) {
            throw new EntityNotFoundException("본인의 댓글만 수정할 수 있습니다.");
        }
        comment.assignContent(requestDTO.getContent());
        Comment updatedComment = commentRepository.save(comment);

        return CommentResponseDTO.ofEntity(updatedComment);
    }

    public List<PostListResponseDto> getPostsAboutCommentByMemberId(Long memberId, Integer page) {
        int pageSize = 20;
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<Post> posts = commentRepository.findPostDistinctByMemberId(memberId, pageable);
        return posts.isEmpty() ? new ArrayList<>() : posts.stream()
                .map(PostListResponseDto::ofEntity)
                .collect(Collectors.toList());
    }

    public CommentResponseDTO getCommentById(Long id) {
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 댓글입니다."));
        return CommentResponseDTO.ofEntity(comment);
    }

}
