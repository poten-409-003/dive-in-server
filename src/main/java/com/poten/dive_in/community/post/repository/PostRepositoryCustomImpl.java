package com.poten.dive_in.community.post.repository;

import com.poten.dive_in.auth.entity.QMember;
import com.poten.dive_in.cmmncode.entity.QCommonCode;
import com.poten.dive_in.community.comment.entity.Comment;
import com.poten.dive_in.community.comment.entity.QComment;
import com.poten.dive_in.community.post.entity.Post;
import com.poten.dive_in.community.post.entity.QPost;
import com.poten.dive_in.community.post.entity.QPostImage;
import com.poten.dive_in.community.post.entity.QPostLike;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class PostRepositoryCustomImpl implements PostRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public Optional<Post> findByIdWithMember(Long id) {
        QPost qPost = QPost.post;
        QMember qMember = QMember.member;
        Post result = queryFactory
                .selectFrom(qPost)
                .join(qPost.member, qMember).fetchJoin()
                .where(qPost.id.eq(id))
                .fetchOne();

        return Optional.ofNullable(result);
    }

    public Optional<Post> findByIdWithDetail(Long id) {
        QPost qPost = QPost.post;
        QMember qMember = QMember.member;
        QCommonCode qCommonCode = QCommonCode.commonCode;
        QComment qComment = QComment.comment;
        QPostImage qPostImage = QPostImage.postImage;
        QPostLike qPostLike = QPostLike.postLike;

        // 댓글 조회
        List<Comment> comments = queryFactory
                .selectFrom(qComment)
                .where(qComment.post.id.eq(id))
                .orderBy(qComment.createdAt.desc()) // 최신 댓글 우선
                .fetch();

        // 댓글을 그룹화하여 대댓글 처리
        Map<Integer, List<Comment>> groupedComments = comments.stream()
                .collect(Collectors.groupingBy(Comment::getGroupName));

        // 각 그룹에서 대댓글만 최근 3개 선택하고, 원본 댓글은 모두 포함
        List<Comment> limitedComments = new ArrayList<>();

        // 원본 댓글 추가
        for (Comment comment : comments) {
            if (comment.getCmntClass() == 0) { // 댓글인 경우
                limitedComments.add(comment);
            }
        }

        // 각 그룹에서 대댓글만 최근 3개 선택
        for (List<Comment> group : groupedComments.values()) {
            List<Comment> replies = group.stream()
                    .filter(c -> c.getCmntClass() == 1) // 대댓글만 필터링
                    .limit(3) // 최근 3개
                    .collect(Collectors.toList());

            limitedComments.addAll(replies); // 선택한 대댓글 추가
        }

        Post result = queryFactory
                .selectFrom(qPost)
                .leftJoin(qPost.member, qMember).fetchJoin()
                .leftJoin(qPost.categoryCode, qCommonCode).fetchJoin()
                .leftJoin(qPost.images, qPostImage).fetchJoin()
                .where(qPost.id.eq(id)
                        .and(qPost.isActive.eq("Y")))
                .fetchOne();

        // 댓글 설정 및 정렬
        if (result != null) {
            result.replaceCommentList(new HashSet<>(limitedComments)); // 수정된 댓글 리스트로 설정
            result.sortComments();
        }

        return Optional.ofNullable(result);
//        List<Comment> comments = queryFactory
//                .selectFrom(qComment)
//                .where(qComment.post.id.eq(id))
//                .distinct()
//                .fetch();
//
//        Post result = queryFactory
//                .selectFrom(qPost)
//                .leftJoin(qPost.member, qMember).fetchJoin()
//                .leftJoin(qPost.categoryCode, qCommonCode).fetchJoin()
//                .leftJoin(qPost.images, qPostImage).fetchJoin()
//                .leftJoin(qPost.likes, qPostLike).fetchJoin()
//                .where(qPost.id.eq(id))
//                .fetchOne();
//
//        // 댓글 설정 및 정렬
//        if (result != null) {
//            result.replaceCommentList(new HashSet<>(comments));
//            result.sortComments();
//        }
//
//        return Optional.ofNullable(result);
    }

    public Page<Post> findActivePosts(Pageable pageable) {
        QPost qPost = QPost.post;
        List<Post> posts = queryFactory
                .selectFrom(qPost)
                .where(qPost.isActive.eq("Y"))
                .orderBy(qPost.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = countActivePosts();

        return new PageImpl<>(posts, pageable, total);
    }

    public Long countActivePosts() {
        QPost qPost = QPost.post;
        return queryFactory
                .selectFrom(qPost)
                .where(qPost.isActive.eq("Y"))
                .stream().count();
    }

    public List<Post> findPopularPosts(LocalDateTime conditionDate) {
        QPost qPost = QPost.post;
        return queryFactory
                .selectFrom(qPost)
                .where(qPost.isActive.eq("Y")
                        .and(qPost.createdAt.goe(conditionDate))
                        .and(qPost.likeCount.goe(1)))
                .orderBy(qPost.likeCount.desc())
                .limit(10)
                .fetch();
    }

    public Set<Long> findPopularPostIds(LocalDateTime conditionDate) {
        QPost qPost = QPost.post;

        return new HashSet<>(queryFactory
                .select(qPost.id)
                .from(qPost)
                .where(qPost.isActive.eq("Y")
                        .and(qPost.createdAt.goe(conditionDate))
                        .and(qPost.likeCount.goe(1)))
                .orderBy(qPost.likeCount.desc())
                .limit(10)
                .fetch());
    }

    public Page<Post> findByCategoryCodeCd(String categoryType, Pageable pageable) {
        QPost qPost = QPost.post;
        List<Post> posts = queryFactory
                .selectFrom(qPost)
                .where(qPost.isActive.eq("Y")
                        .and(qPost.categoryCode.code.eq(categoryType)))
                .orderBy(qPost.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = countPostsByCategoryCode(categoryType);

        return new PageImpl<>(posts, pageable, total);
    }

    public Long countPostsByCategoryCode(String categoryType) {
        QPost qPost = QPost.post;
        return queryFactory
                .selectFrom(qPost)
                .where(qPost.isActive.eq("Y")
                        .and(qPost.categoryCode.code.eq(categoryType)))
                .stream().count();
    }

    // 회원 ID로 게시글 조회
    public Page<Post> findPostsByMemberId(Pageable pageable, Long memberId) {
        QPost qPost = QPost.post;
        List<Post> posts = queryFactory
                .selectFrom(qPost)
                .where(qPost.member.id.eq(memberId)
                        .and(qPost.isActive.eq("Y")))
                .orderBy(qPost.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = countPostsByMemberId(memberId);

        return new PageImpl<>(posts, pageable, total);
    }

    public Long countPostsByMemberId(Long memberId) {
        QPost qPost = QPost.post;
        return queryFactory
                .selectFrom(qPost)
                .where(qPost.member.id.eq(memberId)
                        .and(qPost.isActive.eq("Y")))
                .stream().count();
    }


    @Override
    public List<Post> findNewPosts() {
        QPost qPost = QPost.post;

        return queryFactory
                .selectFrom(qPost)
                .where(qPost.categoryCode.codeName.ne("수영대회").and(qPost.isActive.eq("Y")))
                .orderBy(qPost.createdAt.desc())
                .limit(2)
                .fetch();
    }

    @Override
    public List<Post> findCompetitionPosts() {
        QPost qPost = QPost.post;

        return queryFactory
                .selectFrom(qPost)
                .where(qPost.categoryCode.codeName.eq("수영대회").and(qPost.isActive.eq("Y")))
                .orderBy(qPost.createdAt.desc())
                .limit(3)
                .fetch();
    }

    @Override
    public List<Post> findTopViewPosts() {
        QPost qPost = QPost.post;

        return queryFactory
                .selectFrom(qPost)
                .where(qPost.categoryCode.codeName.ne("수영대회").and(qPost.viewCount.gt(1)).and(qPost.isActive.eq("Y")))
                .orderBy(qPost.viewCount.desc())
                .limit(2)
                .fetch();
    }
    //    public Page<Post> searchPosts(String query, Pageable pageable) {
//        QPost qPost = QPost.post;
//        List<Post> posts = queryFactory
//                .selectFrom(qPost)
//                .where(qPost.title.containsIgnoreCase(query)
//                        .or(qPost.content.containsIgnoreCase(query))
//                        .or(qPost.categoryCode.codeName.containsIgnoreCase(query))
//                        .or(qPost.member.nickname.containsIgnoreCase(query)))
//                .offset(pageable.getOffset())
//                .limit(pageable.getPageSize())
//                .orderBy(qPost.createdAt.desc())
//                .fetch();
//
//        long total = queryFactory
//                .selectFrom(qPost)
//                .where(qPost.title.containsIgnoreCase(query)
//                        .or(qPost.content.containsIgnoreCase(query))
//                        .or(qPost.categoryCode.codeName.containsIgnoreCase(query))
//                        .or(qPost.member.nickname.containsIgnoreCase(query)))
//                .stream().count();
//
//        return new PageImpl<>(posts, pageable, total);
//    }
}

