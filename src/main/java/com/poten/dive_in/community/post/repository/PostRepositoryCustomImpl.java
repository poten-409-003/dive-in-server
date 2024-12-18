package com.poten.dive_in.community.post.repository;

import com.poten.dive_in.auth.entity.QMember;
import com.poten.dive_in.cmmncode.entity.QCommonCode;
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

import java.util.List;
import java.util.Optional;

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
        QPostLike  qPostLike = QPostLike.postLike;

        Post result = queryFactory
                .selectFrom(qPost)
                .join(qPost.member, qMember).fetchJoin() // Member와의 fetch join
                .join(qPost.categoryCode, qCommonCode).fetchJoin() // CategoryCode와의 fetch join
                .join(qPost.comments, qComment).fetchJoin() // Comments와의 fetch join
                .join(qPost.images, qPostImage).fetchJoin() // Images와의 fetch join
                .join(qPost.likes, qPostLike).fetchJoin() // Likes와의 fetch join
                .where(qPost.id.eq(id))
                .fetchOne();

        return Optional.ofNullable(result);
    }

    public Page<Post> findActivePosts(Pageable pageable) {
        QPost qPost = QPost.post;
        List<Post> posts = queryFactory
                .selectFrom(qPost)
                .where(qPost.isActive.eq("Y")) // 예시: 활성 상태가 "Y"인 경우
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory
                .selectFrom(qPost)
                .where(qPost.isActive.eq("Y"))
                .stream().count();

        return new PageImpl<>(posts, pageable, total);
    }

    public Page<Post> findPopularPosts(Pageable pageable) {
        QPost qPost = QPost.post;
        List<Post> posts = queryFactory
                .selectFrom(qPost)
                .where(qPost.viewCount.goe(10))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory
                .selectFrom(qPost)
                .stream().count();

        return new PageImpl<>(posts, pageable, total);
    }

    public Page<Post> findByCategoryCodeCd(String categoryType, Pageable pageable) {
        QPost qPost = QPost.post;
        List<Post> posts = queryFactory
                .selectFrom(qPost)
                .where(qPost.categoryCode.code.eq(categoryType)) // 예시: 카테고리 코드
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory
                .selectFrom(qPost)
                .where(qPost.categoryCode.code.eq(categoryType))
                .stream().count();

        return new PageImpl<>(posts, pageable, total);
    }

    // 회원 ID로 게시글 조회
    public Page<Post> findPostsByMemberId(Pageable pageable, Long memberId) {
        QPost qPost = QPost.post;
        List<Post> posts = queryFactory
                .selectFrom(qPost)
                .where(qPost.member.id.eq(memberId)) // 회원 ID로 필터링
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory
                .selectFrom(qPost)
                .where(qPost.member.id.eq(memberId))
                .stream().count();

        return new PageImpl<>(posts, pageable, total);
    }
//    public List<Post> findActivePosts() {
//        QPost qPost = QPost.post;
//        return queryFactory.selectFrom(qPost)
//                .where(qPost.isActive.eq("Y"))
//                .orderBy(qPost.createdAt.desc())
//                .fetch();
//    }
//
//    public List<Post> findPopularPosts() {
//        QPost qPost = QPost.post;
//        return queryFactory.selectFrom(qPost)
//                .where(qPost.viewCount.goe(10))
//                .orderBy(qPost.createdAt.desc())
//                .fetch();
//    }
//
//    public List<Post> findPostsByMemberId(Long memberId) {
//        QPost qPost = QPost.post;
//        return queryFactory.selectFrom(qPost)
//                .where(qPost.member.id.eq(memberId))
//                .orderBy(qPost.createdAt.desc())
//                .fetch();
//    }
}

