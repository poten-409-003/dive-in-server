package com.poten.dive_in.community.comment.service;

import com.poten.dive_in.auth.entity.Member;
import com.poten.dive_in.auth.repository.MemberRepository;
import com.poten.dive_in.community.comment.dto.CommentRequestDTO;
import com.poten.dive_in.community.comment.dto.CommentResponseDTO;
import com.poten.dive_in.community.comment.entity.Comment;
import com.poten.dive_in.community.comment.repository.CommentLikeRepository;
import com.poten.dive_in.community.comment.repository.CommentRepository;
import com.poten.dive_in.community.post.dto.PostListResponseDto;
import com.poten.dive_in.community.post.dto.PostResponseDto;
import com.poten.dive_in.community.post.entity.Post;
import com.poten.dive_in.community.post.repository.PostRepository;
import com.vane.badwordfiltering.BadWordFiltering;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final CommentLikeRepository commentLikeRepository;
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;


    @Transactional
    public CommentResponseDTO createComment(CommentRequestDTO requestDTO) {
        Member member = memberRepository.findById(requestDTO.getMemberId()).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 회원입니다."));
        Post post = postRepository.findById(requestDTO.getPostId()).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 글입니다."));
        post.adjustCommentCount(1);
        String filteredContent = badwordFiltering(requestDTO.getContent());
        Comment comment = Comment.builder()
                .post(post)
                .member(member)
                .content(filteredContent)
                .cmntClass(0) // 댓글로 설정
                .orderNumber(0)
                .likeCount(0)
                .isActive("Y")
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
        post.adjustCommentCount(1);
        // 부모 댓글 조회
        Comment parentComment = commentRepository.findById(parentId)
                .orElseThrow(() -> new EntityNotFoundException("댓글이 존재하지 않습니다."));

        // 대댓글의 그룹 번호와 순서 번호 설정
        Integer groupName = parentComment.getGroupName();
        Integer orderNumber = commentRepository.countByGroupName(groupName).intValue();
        String filteredContent = badwordFiltering(requestDTO.getContent());

        Comment reply = Comment.builder()
                .member(member)
                .post(post)
                .content(filteredContent)
                .groupName(groupName)
                .orderNumber(orderNumber) // 순서 번호 설정
                .cmntClass(1) // 대댓글로 설정
                .likeCount(0)
                .isActive("Y")
                .build();

        Comment savedReply = commentRepository.save(reply);

        return CommentResponseDTO.ofEntity(savedReply);
    }

    @Transactional
    public void deleteComment(Long commentId, Long memberId) {
        //댓글 존재 확인
        Comment comment = commentRepository.findByIdWithMemberAndPost(commentId)
                .orElseThrow(() -> new EntityNotFoundException("댓글이 존재하지 않습니다."));

        // 작성자 확인
        if (!comment.getMember().getId().equals(memberId)) {
            throw new EntityNotFoundException("본인의 댓글만 삭제할 수 있습니다.");
        }

        Post post = comment.getPost();

        if (comment.getCmntClass() == 0) {
            List<Comment> replies = commentRepository.findCommentsByGroupName(comment.getGroupName());
            for (Comment reply : replies) {
                commentRepository.delete(reply);
            }
            int deleteCnt = replies.size();
            post.adjustCommentCount(-deleteCnt);
        } else {
            post.adjustCommentCount(-1);
        }
        comment.assignPost(post);
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
        String filteredContent = badwordFiltering(requestDTO.getContent());

        comment.assignContent(filteredContent);
        Comment updatedComment = commentRepository.save(comment);

        Long memberId = requestDTO.getMemberId();

        boolean pushLike = memberId != null && commentLikeRepository.existsByCommentIdAndMemberId(updatedComment.getId(), memberId);
        CommentResponseDTO commentResponseDTO = CommentResponseDTO.ofEntity(updatedComment);
        commentResponseDTO.assignIsLiked(pushLike);

        return commentResponseDTO;
    }

    @Transactional(readOnly = true)
    public PostListResponseDto getPostsAboutCommentByMemberId(Long memberId, Integer page) {
        int pageSize = 10;

        LocalDateTime oneMonthAgo = LocalDateTime.now().minusMonths(1);
        Pageable pageable = PageRequest.of(page, pageSize);

        Set<Long> popularPostIds = postRepository.findPopularPostIds(oneMonthAgo);

        Page<Post> posts = commentRepository.findDistinctPostsByMemberId(memberId, pageable);
        Long totalPosts = commentRepository.countPostByComment(memberId);
        boolean hasMore = posts.hasNext();

        List<PostResponseDto> postResponseDtos = posts.isEmpty() ? new ArrayList<>() : posts.stream()
                .map(post -> PostResponseDto.ofEntity(post, popularPostIds.contains(post.getId())))
                .collect(Collectors.toList());
        return PostListResponseDto.toPostListResponseDto(postResponseDtos, totalPosts, hasMore);
    }

    @Transactional(readOnly = true)
    public List<CommentResponseDTO> getCommentById(Long id, Long memberId) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 댓글입니다."));

        List<Comment> comments = commentRepository.findCommentsById(id);

        comments.sort(Comparator.comparing(Comment::getCmntClass)
                .thenComparing(Comment::getOrderNumber));

        return comments.stream()
                .map(c -> {
                    boolean isLiked = memberId != null && commentLikeRepository.existsByCommentIdAndMemberId(c.getId(), memberId);
                    return CommentResponseDTO.ofEntity(c, isLiked);
                })
                .collect(Collectors.toList());
    }

    private String badwordFiltering(String original) {
        BadWordFiltering badWordFiltering = new BadWordFiltering();
        // 욕 사이의 공백, 숫자, 특수기호 제거
        String cleanedInput = original.replaceAll("[\\s0-9!@#$%^&*()_+=-]", "");
        if (badWordFiltering.check(cleanedInput)) {
            return badWordFiltering.change(cleanedInput);
        }
        return original;
    }


}
