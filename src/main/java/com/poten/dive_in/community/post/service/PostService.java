package com.poten.dive_in.community.post.service;


import com.poten.dive_in.auth.entity.Member;
import com.poten.dive_in.auth.repository.MemberRepository;
import com.poten.dive_in.cmmncode.entity.CommonCode;
import com.poten.dive_in.cmmncode.repository.CmmnCdRepository;
import com.poten.dive_in.common.service.S3Service;
import com.poten.dive_in.community.post.dto.PostListResponseDto;
import com.poten.dive_in.community.post.enums.CategoryType;
import com.poten.dive_in.community.post.dto.PostRequestDto;
import com.poten.dive_in.community.post.dto.PostDetailResponseDto;
import com.poten.dive_in.community.post.entity.Post;
import com.poten.dive_in.community.post.entity.PostImage;
import com.poten.dive_in.community.post.repository.PostLikeRepository;
import com.poten.dive_in.community.post.repository.PostRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
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
    public PostDetailResponseDto createPost(PostRequestDto postRequestDTO) {
        Member member = memberRepository.findById(postRequestDTO.getMemberId()).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 회원입니다."));

        String categoryName = CategoryType.findKoreanNameByInput(postRequestDTO.getCategoryType());
        if (categoryName == null) {
            throw new IllegalArgumentException("해당 카테고리가 존재하지 않습니다.");
        }
        CommonCode commonCode = cmmnCdRepository.findByCodeName(categoryName).orElseThrow(() -> new EntityNotFoundException("해당 카테고리가 공통코드에 존재하지 않습니다."));

        Post post = Post.builder()
                .title(postRequestDTO.getTitle())
                .content(postRequestDTO.getContent())
                .categoryCode(commonCode)
                .member(member)
                .build();

        // 이미지가 있는 경우만
        List<MultipartFile> multipartFileList = postRequestDTO.getImages();
        if (multipartFileList !=null && !multipartFileList.isEmpty()){
            Set<PostImage> postImageList = uploadAndCreatePostImages(multipartFileList,post);

            post.addImage(postImageList);
        }
        Post savedPost = postRepository.save(post);
        return PostDetailResponseDto.ofEntity(savedPost);
    }

    @Transactional
    public void deletePost(Long id, Long memberId) {
        Post post = postRepository.findByIdWithMember(id)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));
        // 작성자 확인
        if (!post.getMember().getId().equals(memberId)) {
            throw new IllegalArgumentException("자신의 글만 삭제 가능합니다.");
        }
        postRepository.delete(post);
    }

    @Transactional
    public Post updatePost(Long id, PostRequestDto requestDTO) {
        Post existingPost = postRepository.findByIdWithMember(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 글이 존재하지 않습니다."));

        if (!existingPost.getMember().getId().equals(requestDTO.getMemberId())) {
            throw new IllegalArgumentException("자신의 글만 수정 가능합니다.");
        }

        CommonCode commonCode = cmmnCdRepository.findByCodeName(CategoryType.findKoreanNameByInput(requestDTO.getCategoryType())).orElseThrow(() -> new EntityNotFoundException("해당 카테고리가 존재하지 않습니다."));

        // 새로운 Post 객체 생성
        Post newPost = Post.builder()
                .id(existingPost.getId())
                .member(existingPost.getMember())
                .title(requestDTO.getTitle())
                .content(requestDTO.getContent())
                .categoryCode(commonCode)
                .commentCount(existingPost.getCommentCount())
                .likeCount(existingPost.getLikeCount())
                .viewCount(existingPost.getViewCount())
                .isActive(existingPost.getIsActive())
                .images(existingPost.getImages())
                .build();

        List<MultipartFile> multipartFileList = requestDTO.getImages();
        if (multipartFileList != null && !multipartFileList.isEmpty()) {
            deletePostImagesFromS3(newPost.getImages());
            Set<PostImage> postImageList = uploadAndCreatePostImages(multipartFileList, newPost);
            newPost.replaceImageList(postImageList);
        }

        return postRepository.save(newPost);
    }

    public List<PostListResponseDto> getAllPosts(String categoryType, int page) {
        int pageSize = 20; // 한 페이지당 게시물 수
        Page<Post> posts;
        Pageable pageable = PageRequest.of(page, pageSize);
        if ("none".equals(categoryType)) {
            posts = postRepository.findActivePosts(pageable);

        } else if ("popular".equals(categoryType)) {
            posts = postRepository.findPopularPosts(pageable);
        } else {
            posts = postRepository.findByCategoryCodeCd(categoryType, pageable);
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

    public List<PostListResponseDto> getPostsByUserId(Long userId, Integer page) {
        int pageSize = 20; // 한 페이지당 게시물 수
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
            boolean repImage = (i == 0); // 첫 번째 이미지를 대표 이미지로 설정

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

}


