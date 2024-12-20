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

import java.util.HashSet;
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
        QPostLike qPostLike = QPostLike.postLike;

//        Post result = queryFactory
//                .selectFrom(qPost)
//                .leftJoin(qPost.member, qMember).fetchJoin()
//                .leftJoin(qPost.categoryCode, qCommonCode).fetchJoin()
//                .leftJoin(qPost.comments, qComment).fetchJoin()
//                .leftJoin(qPost.images, qPostImage).fetchJoin()
//                .leftJoin(qPost.likes, qPostLike).fetchJoin()
//                .where(qPost.id.eq(id))
//                .orderBy(qComment.groupName.asc(),
//                        qComment.cmntClass.asc(),
//                        qComment.orderNumber.asc()
//                )
//                .fetchOne();
        // 서브쿼리로 중복된 댓글을 가져옴
        List<Comment> comments = queryFactory
                .selectFrom(qComment)
                .where(qComment.post.id.eq(id))
                .distinct() // 중복 제거
                .fetch();

        Post result = queryFactory
                .selectFrom(qPost)
                .leftJoin(qPost.member, qMember).fetchJoin()
                .leftJoin(qPost.categoryCode, qCommonCode).fetchJoin()
                .leftJoin(qPost.images, qPostImage).fetchJoin()
                .leftJoin(qPost.likes, qPostLike).fetchJoin()
                .where(qPost.id.eq(id))
                .fetchOne();

        // 댓글 설정 및 정렬
        if (result != null) {
            result.replaceCommentList(new HashSet<>(comments)); // Set으로 중복 제거
            result.sortComments(); // 댓글 정렬
        }

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

