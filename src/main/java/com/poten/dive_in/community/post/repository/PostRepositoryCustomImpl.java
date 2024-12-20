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

        List<Comment> comments = queryFactory
                .selectFrom(qComment)
                .where(qComment.post.id.eq(id))
                .distinct()
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
            result.replaceCommentList(new HashSet<>(comments));
            result.sortComments();
        }

        return Optional.ofNullable(result);
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

        long total = queryFactory
                .selectFrom(qPost)
                .where(qPost.isActive.eq("Y"))
                .stream().count();

        return new PageImpl<>(posts, pageable, total);
    }

    public List<Post> findPopularPosts(Pageable pageable) {
        QPost qPost = QPost.post;
        return queryFactory
                .selectFrom(qPost)
                .where(qPost.viewCount.goe(10))
                .orderBy(qPost.viewCount.desc(), qPost.createdAt.desc())
                .limit(10)
                .fetch();
    }

    public Page<Post> findByCategoryCodeCd(String categoryType, Pageable pageable) {
        QPost qPost = QPost.post;
        List<Post> posts = queryFactory
                .selectFrom(qPost)
                .where(qPost.categoryCode.code.eq(categoryType))
                .orderBy(qPost.createdAt.desc())
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
                .where(qPost.member.id.eq(memberId))
                .orderBy(qPost.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory
                .selectFrom(qPost)
                .where(qPost.member.id.eq(memberId))
                .stream().count();

        return new PageImpl<>(posts, pageable, total);
    }

    public Page<Post> searchPosts(String query, Pageable pageable) {
        QPost qPost = QPost.post;
        List<Post> posts = queryFactory
                .selectFrom(qPost)
                .where(qPost.title.containsIgnoreCase(query)
                        .or(qPost.content.containsIgnoreCase(query))
                        .or(qPost.categoryCode.codeName.containsIgnoreCase(query))
                        .or(qPost.member.nickname.containsIgnoreCase(query)))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(qPost.createdAt.desc())
                .fetch();

        long total = queryFactory
                .selectFrom(qPost)
                .where(qPost.title.containsIgnoreCase(query)
                        .or(qPost.content.containsIgnoreCase(query))
                        .or(qPost.categoryCode.codeName.containsIgnoreCase(query))
                        .or(qPost.member.nickname.containsIgnoreCase(query)))
                .stream().count();

        return new PageImpl<>(posts, pageable, total);
    }
}

