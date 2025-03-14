package com.poten.dive_in.community.post.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.poten.dive_in.auth.entity.Member;
import com.poten.dive_in.auth.repository.MemberRepository;
import com.poten.dive_in.cmmncode.entity.CommonCode;
import com.poten.dive_in.cmmncode.repository.CmmnCdRepository;
import com.poten.dive_in.common.service.S3Service;
import com.poten.dive_in.community.comment.dto.CommentResponseDTO;
import com.poten.dive_in.community.comment.entity.Comment;
import com.poten.dive_in.community.comment.repository.CommentLikeRepository;
import com.poten.dive_in.community.comment.repository.CommentRepository;
import com.poten.dive_in.community.post.dto.*;
import com.poten.dive_in.community.post.entity.Post;
import com.poten.dive_in.community.post.entity.PostImage;
import com.poten.dive_in.community.post.enums.CategoryType;
import com.poten.dive_in.community.post.repository.PostLikeRepository;
import com.poten.dive_in.community.post.repository.PostRepository;
import com.vane.badwordfiltering.BadWordFiltering;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.poten.dive_in.common.service.S3Service.extractFileName;

@Service
@RequiredArgsConstructor
public class PostService {
    private final S3Service s3Service;
    private final PostRepository postRepository;
    private final CmmnCdRepository cmmnCdRepository;
    private final MemberRepository memberRepository;
    private final PostLikeRepository postLikeRepository;
    private final CommentRepository commentRepository;
    private final CommentLikeRepository commentLikeRepository;

    @Transactional
    public PostDetailResponseDto createPost(PostRequestDto postRequestDTO, List<MultipartFile> multipartFileList, String email) {
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 회원입니다."));

        String categoryName = CategoryType.findKoreanNameByInput(postRequestDTO.getCategoryType());
        if (categoryName == null) {
            throw new IllegalArgumentException("해당 카테고리가 존재하지 않습니다.");
        }
        if ("수영대회".equals(categoryName)) {
            if (member.getRole().getId() == 1) {
                throw new IllegalArgumentException("글 작성 권한이 없습니다.");
            }
        }
        CommonCode commonCode = cmmnCdRepository.findByCodeName(categoryName).orElseThrow(() -> new EntityNotFoundException("해당 카테고리가 공통코드에 존재하지 않습니다."));

        String filteredTitle = badwordFiltering(postRequestDTO.getTitle());
        String filteredContent = badwordFiltering(postRequestDTO.getContent());
        Post post = Post.builder()
                .title(filteredTitle)
                .content(filteredContent)
                .categoryCode(commonCode)
                .member(member)
                .likeCount(0)
                .commentCount(0)
                .viewCount(0)
                .isActive("Y")
                .build();


        if (multipartFileList != null && !multipartFileList.isEmpty()) {
            boolean isNotEmpty = true;
            for (MultipartFile file : multipartFileList) {
                if (file.isEmpty()) {
                    isNotEmpty = false;
                    break;
                }
            }
            if (isNotEmpty) {
                Set<PostImage> postImageList = uploadAndCreatePostImages(multipartFileList, post);
                post.addImage(postImageList);
            }
        }
        Post savedPost = postRepository.save(post);
        return PostDetailResponseDto.ofEntity(savedPost);
    }

    @Transactional
    public void deletePost(Long id, String email) {
        Post post = postRepository.findByIdWithMember(id)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 회원입니다."));
        if ("수영대회".equals(post.getCategoryCode().getCodeName())) {
            if (member.getRole().getId() == 1) {
                throw new IllegalArgumentException("글 삭제 권한이 없습니다.");
            }
        }

        if (!member.getId().equals(post.getMember().getId())) {
            throw new IllegalArgumentException("자신의 글만 삭제 가능합니다.");
        }
        deletePostImagesFromS3(post.getImages().stream()
                .map(PostImageDto::ofEntity)
                .collect(Collectors.toList()));
        postRepository.delete(post);
    }

    @Transactional
    public PostDetailResponseDto updatePost(Long id, PostEditRequestDto requestDTO, List<MultipartFile> multipartFileList, String email) {
        Post existingPost = postRepository.findByIdWithDetail(id)
                .orElseThrow(() -> new EntityNotFoundException("해당 글이 존재하지 않습니다."));
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 회원입니다."));
        if ("수영대회".equals(existingPost.getCategoryCode().getCodeName())) {
            if (member.getRole().getId() == 1) {
                throw new IllegalArgumentException("글 수정 권한이 없습니다.");
            }
        }
        if (!member.getId().equals(existingPost.getMember().getId())) {
            throw new IllegalArgumentException("자신의 글만 수정 가능합니다.");
        }
        ObjectMapper objectMapper = new ObjectMapper();

        List<PostImageDto> existingImages = null;
        try {
            existingImages = objectMapper.readValue(requestDTO.getExistingImages(), new TypeReference<List<PostImageDto>>() {
            });
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        Set<PostImage> imagesToRemove = null;
        if (existingImages != null && !existingImages.isEmpty()) {
            Set<String> requestImageUrls = existingImages.stream()
                    .map(PostImageDto::getImageUrl)
                    .collect(Collectors.toSet());

            imagesToRemove = new HashSet<>(existingPost.getImages().stream()
                    .filter(postImage -> !requestImageUrls.contains(postImage.getImageUrl()))
                    .collect(Collectors.toSet()));
        } else {
            imagesToRemove = new HashSet<>(existingPost.getImages());
        }
        for (PostImage image : imagesToRemove) {
            String fileName = extractFileName(image.getImageUrl());
            s3Service.deleteFile(fileName);
            existingPost.getImages().remove(image);
        }


        CommonCode commonCode = cmmnCdRepository.findByCodeName(CategoryType.findKoreanNameByInput(requestDTO.getCategoryType())).orElseThrow(() -> new EntityNotFoundException("해당 카테고리가 존재하지 않습니다."));

        String filteredTitle = badwordFiltering(requestDTO.getTitle());
        String filteredContent = badwordFiltering(requestDTO.getContent());

        existingPost.updatePost(commonCode, filteredContent, filteredTitle);

        if (multipartFileList != null && !multipartFileList.isEmpty()) {
            boolean isNotEmpty = true;
            for (MultipartFile file : multipartFileList) {
                if (file.isEmpty()) {
                    isNotEmpty = false;
                    break;
                }
            }
            if (isNotEmpty) {
                Set<PostImage> postImageList = uploadAndCreatePostImages(multipartFileList, existingPost, existingImages.stream().noneMatch(PostImageDto::getRepImage));
                existingPost.appendImageList(postImageList);
            }
        }

        Long memberId = member.getId();
        Post updatedPost = postRepository.save(existingPost);
        PostDetailResponseDto detailResponseDto = PostDetailResponseDto.ofEntity(updatedPost);

        for (CommentResponseDTO comment : detailResponseDto.getCommentList()) {
            boolean pushLike = memberId != null && commentLikeRepository.existsByCommentIdAndMemberId(comment.getCmntId(), memberId);
            comment.assignIsLiked(pushLike);
        }

        LocalDateTime oneMonthAgo = LocalDateTime.now().minusMonths(1);
        Set<Long> popularPostIds = postRepository.findPopularPostIds(oneMonthAgo);

        Boolean pushLike = postLikeRepository.existsByPostIdAndMemberId(updatedPost.getId(), memberId);
        detailResponseDto.assignIsLiked(pushLike);
        detailResponseDto.assignIsPopular(popularPostIds.contains(updatedPost.getId()));
        return detailResponseDto;
    }

    @Transactional(readOnly = true)
    public PostListResponseDto getAllPosts(String categoryType, int page) {
        int sizePerPage = 10;
        Page<Post> posts;

        LocalDateTime oneMonthAgo = LocalDateTime.now().minusMonths(1);
        Pageable pageable = PageRequest.of(page, sizePerPage);

        Set<Long> popularPostIds = postRepository.findPopularPostIds(oneMonthAgo);

        Long totalPosts = null;
        boolean hasMore = false;

        if ("none".equals(categoryType)) {
            posts = postRepository.findActivePosts(pageable);
            totalPosts = postRepository.countActivePosts();
            hasMore = posts.hasNext();
        } else if ("popular".equals(categoryType)) {
            List<Post> popularPosts = postRepository.findPopularPosts(oneMonthAgo);
            totalPosts = (long) popularPosts.size();
            List<PostResponseDto> popularPostDtos = popularPosts.stream()
                    .map(post -> PostResponseDto.ofEntity(post, true))
                    .collect(Collectors.toList());

            return PostListResponseDto.toPostListResponseDto(popularPostDtos, totalPosts, hasMore);
        } else {
            String koreanName = CategoryType.findKoreanNameByInput(categoryType);
            CommonCode code = cmmnCdRepository.findByCodeName(koreanName)
                    .orElseThrow(() -> new EntityNotFoundException("카테고리가 존재하지 않습니다."));

            posts = postRepository.findByCategoryCodeCd(code.getCode(), pageable);
            totalPosts = postRepository.countPostsByCategoryCode(code.getCode());
            hasMore = posts.hasNext();
        }

        List<PostResponseDto> postDtos = posts.isEmpty() ? new ArrayList<>() : posts.stream()
                .map(post -> PostResponseDto.ofEntity(post, popularPostIds.contains(post.getId())))
                .collect(Collectors.toList());
        return PostListResponseDto.toPostListResponseDto(postDtos, totalPosts, hasMore);
    }

    @Transactional(readOnly = true)
    public PostDetailResponseDto getPostById(Long id, String email) {
//        addViewCnt(id);
        Post post = postRepository.findByIdWithDetail(id).orElseThrow(() -> new EntityNotFoundException("해당 글이 존재하지 않습니다."));
        boolean isLiked = false;
        if (email != null) {
            Member member = memberRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 회원입니다."));
            isLiked = postLikeRepository.existsByPostIdAndMemberId(post.getId(), member.getId());
        }

        PostDetailResponseDto detailResponseDto = PostDetailResponseDto.ofEntity(post, isLiked);

        LocalDateTime oneMonthAgo = LocalDateTime.now().minusMonths(1);
        Set<Long> popularPostIds = postRepository.findPopularPostIds(oneMonthAgo);


        detailResponseDto.assignIsPopular(popularPostIds.contains(post.getId()));

        List<Comment> comments = commentRepository.findCommentsWithReplyCountByPostId(id);

        Map<Integer, Long> replyCountMap = comments.stream()
                .filter(c -> c.getCmntClass() == 1)
                .collect(Collectors.groupingBy(Comment::getGroupName, Collectors.counting()));

        for (CommentResponseDTO comment : detailResponseDto.getCommentList()) {
            Long replyCount = replyCountMap.get(comment.getGroupName());
            if (comment.getCmntClass() == 0) {
                if (replyCount != null) {
                    comment.assignRemainReplyCnt(replyCount >= 3 ? replyCount.intValue() - 3 : replyCount.intValue());
                } else {
                    comment.assignRemainReplyCnt(0);
                }
            }
        }

        return detailResponseDto;

    }

    @Transactional(readOnly = true)
    public PostListResponseDto getPostsByMemberId(String email, Integer page) {
        int pageSize = 10;
        Pageable pageable = PageRequest.of(page, pageSize);

        LocalDateTime oneMonthAgo = LocalDateTime.now().minusMonths(1);
        Set<Long> popularPostIds = postRepository.findPopularPostIds(oneMonthAgo);

        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 회원입니다.")); // ✅ email로 Member 조회
        Long memberId = member.getId();

        Page<Post> posts = postRepository.findPostsByMemberId(pageable, memberId);
        Long totalPosts = postRepository.countPostsByMemberId(memberId);
        boolean hasMore = posts.hasNext();

        List<PostResponseDto> postResponseDtos = posts.isEmpty() ? new ArrayList<>() : posts.stream()
                .map(post -> PostResponseDto.ofEntity(post, popularPostIds.contains(post.getId())))
                .collect(Collectors.toList());

        return PostListResponseDto.toPostListResponseDto(postResponseDtos, totalPosts, hasMore);
    }


    private Set<PostImage> uploadAndCreatePostImages(List<MultipartFile> multipartFileList, Post post) {
        Set<PostImage> postImageList = new HashSet<>();
        List<String> uploadFileList = s3Service.uploadFile(multipartFileList);

        for (int i = 0; i < uploadFileList.size(); i++) {
            boolean repImage = (i == 0);

            PostImage postImage = PostImage.builder()
                    .imageUrl(uploadFileList.get(i))
                    .isRepresentative(repImage ? "Y" : "N")
                    .post(post)
                    .build();

            postImageList.add(postImage);
        }

        return postImageList;
    }

    private Set<PostImage> uploadAndCreatePostImages(List<MultipartFile> multipartFileList, Post post, Boolean notExistRepImage) {
        Set<PostImage> postImageList = new HashSet<>();
        List<String> uploadFileList = s3Service.uploadFile(multipartFileList);

        for (int i = 0; i < uploadFileList.size(); i++) {
            boolean repImage = (i == 0);

            PostImage postImage = PostImage.builder()
                    .imageUrl(uploadFileList.get(i))
                    .isRepresentative(notExistRepImage && repImage ? "Y" : "N")
                    .post(post)
                    .build();

            postImageList.add(postImage);
        }

        return postImageList;
    }

    private void deletePostImagesFromS3(List<PostImageDto> postImageList) {
        if (postImageList != null && !postImageList.isEmpty()) {
            postImageList.forEach(postImage -> {
                String fileName = extractFileName(postImage.getImageUrl());
                s3Service.deleteFile(fileName);
            });
        }
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

    @Transactional
    public void addViewCnt(Long id) {
        Post originPost = postRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("해당 글이 존재하지 않습니다."));
        originPost.adjustViewCount();
        postRepository.save(originPost);
    }
}