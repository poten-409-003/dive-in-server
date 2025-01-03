package com.poten.dive_in.community.post.service;


import com.poten.dive_in.auth.entity.Member;
import com.poten.dive_in.auth.repository.MemberRepository;
import com.poten.dive_in.cmmncode.entity.CommonCode;
import com.poten.dive_in.cmmncode.repository.CmmnCdRepository;
import com.poten.dive_in.common.service.S3Service;
import com.poten.dive_in.community.post.dto.PostDetailResponseDto;
import com.poten.dive_in.community.post.dto.PostListResponseDto;
import com.poten.dive_in.community.post.dto.PostRequestDto;
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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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

    @Transactional
    public PostDetailResponseDto createPost(PostRequestDto postRequestDTO, List<MultipartFile> multipartFileList) {
        Member member = memberRepository.findById(postRequestDTO.getMemberId()).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 회원입니다."));

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

        // 이미지가 있는 경우만
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
    public void deletePost(Long id, Long memberId) {
        Post post = postRepository.findByIdWithMember(id)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));
        Member member = post.getMember();
        if ("수영대회".equals(post.getCategoryCode().getCodeName())) {
            if (member.getRole().getId() == 1) {
                throw new IllegalArgumentException("글 삭제 권한이 없습니다.");
            }
        }

        if (!member.getId().equals(memberId)) {
            throw new IllegalArgumentException("자신의 글만 삭제 가능합니다.");
        }

        postRepository.delete(post);
    }

    @Transactional
    public PostDetailResponseDto updatePost(Long id, PostRequestDto requestDTO, List<MultipartFile> multipartFileList) {
        Post existingPost = postRepository.findByIdWithDetail(id)
                .orElseThrow(() -> new EntityNotFoundException("해당 글이 존재하지 않습니다."));
        Member member = existingPost.getMember();
        if ("수영대회".equals(existingPost.getCategoryCode().getCodeName())) {
            if (member.getRole().getId() == 1) {
                throw new IllegalArgumentException("글 수정 권한이 없습니다.");
            }
        }
        if (!member.getId().equals(requestDTO.getMemberId())) {
            throw new IllegalArgumentException("자신의 글만 수정 가능합니다.");
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
                deletePostImagesFromS3(existingPost.getImages());
                Set<PostImage> postImageList = uploadAndCreatePostImages(multipartFileList, existingPost);
                existingPost.replaceImageList(postImageList);
            }

        }
        Post updatedPost = postRepository.save(existingPost);
        return PostDetailResponseDto.ofEntity(updatedPost);
    }

    @Transactional(readOnly = true)
    public List<PostListResponseDto> getAllPosts(String categoryType, int page) {
        int sizePerPage = 10;
        Page<Post> posts;

        Pageable pageable = PageRequest.of(page, sizePerPage);
        if ("none".equals(categoryType)) {
            posts = postRepository.findActivePosts(pageable);

        } else if ("popular".equals(categoryType)) {
            List<Post> popularPosts = postRepository.findPopularPosts(pageable);
            return popularPosts.isEmpty() ? new ArrayList<>() : popularPosts.stream()
                    .map(PostListResponseDto::ofEntity)
                    .collect(Collectors.toList());
        } else {
            String koreanName = CategoryType.findKoreanNameByInput(categoryType);
            CommonCode code = cmmnCdRepository.findByCodeName(koreanName).orElseThrow(() -> new EntityNotFoundException("카테고리가 존재하지 않습니다."));

            posts = postRepository.findByCategoryCodeCd(code.getCode(), pageable);
        }
        return posts.isEmpty() ? new ArrayList<>() : posts.stream()
                .map(PostListResponseDto::ofEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public PostDetailResponseDto getPostById(Long id) {
        Post post = postRepository.findByIdWithDetail(id).orElseThrow(() -> new IllegalArgumentException("해당 글이 존재하지 않습니다."));
        post.adjustViewCount();
        postRepository.save(post);
        PostDetailResponseDto detailResponseDto = PostDetailResponseDto.ofEntity(post);
        Boolean pushLike = postLikeRepository.existsByPostIdAndMemberId(post.getId(), post.getMember().getId());
        detailResponseDto.assignIsLiked(pushLike);
        return detailResponseDto;

    }

    @Transactional(readOnly = true)
    public List<PostListResponseDto> getPostsByUserId(Long userId, Integer page) {
        int pageSize = 10;
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<Post> posts = postRepository.findPostsByMemberId(pageable, userId);
        return posts.isEmpty() ? new ArrayList<>() : posts.stream()
                .map(PostListResponseDto::ofEntity)
                .collect(Collectors.toList());
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

    private void deletePostImagesFromS3(Set<PostImage> postImageList) {
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

    @Transactional(readOnly = true)
    public List<PostListResponseDto> searchPosts(String query, Integer page) {
        int sizePerPage = 10;
        if (query == null || query.trim().isEmpty()) {
            return List.of();
        }

        Pageable pageable = PageRequest.of(page, sizePerPage);
        Page<Post> posts = postRepository.searchPosts(query, pageable);

        return posts.stream()
                .map(PostListResponseDto::ofEntity)
                .collect(Collectors.toList());
    }
}