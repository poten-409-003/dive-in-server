package com.poten.dive_in.community.comment.repository;

import com.poten.dive_in.auth.entity.QMember;
import com.poten.dive_in.community.comment.entity.Comment;
import com.poten.dive_in.community.comment.entity.QComment;
import com.poten.dive_in.community.post.entity.Post;
import com.poten.dive_in.community.post.entity.PostImage;
import com.poten.dive_in.community.post.entity.QPost;
import com.poten.dive_in.community.post.entity.QPostImage;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class CommentRepositoryCustomImpl implements CommentRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    public Optional<Comment> findByIdWithMember(Long id) {
        QComment qComment = QComment.comment;
        QMember qMember = QMember.member;
        Comment result = queryFactory
                .selectFrom(qComment)
                .join(qComment.member, qMember).fetchJoin()
                .where(qComment.id.eq(id))
                .fetchOne();

        return Optional.ofNullable(result);
    }

    public Optional<Comment> findByIdWithMemberAndPost(Long id) {
        QComment qComment = QComment.comment;
        QMember qMember = QMember.member;
        QPost qPost = QPost.post;
        Comment result = queryFactory
                .selectFrom(qComment)
                .join(qComment.member, qMember).fetchJoin()
                .join(qComment.post, qPost).fetchJoin()
                .where(qComment.id.eq(id))
                .fetchOne();

        return Optional.ofNullable(result);
    }

    @Override
    public Page<Post> findDistinctPostsByMemberId(Long memberId, Pageable pageable) {
        QComment qComment = QComment.comment;
        QPost qPost = QPost.post;
        QPostImage qPostImage = QPostImage.postImage;

        List<Post> posts = queryFactory
                .select(qComment.post)
                .distinct()
                .from(qComment)
                .join(qComment.post, qPost)
                .where(qComment.member.id.eq(memberId))
                .orderBy(qComment.post.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        for (Post post : posts) {
            List<PostImage> images = queryFactory
                    .select(qPostImage)
                    .from(qPostImage)
                    .where(qPostImage.post.id.eq(post.getId()))
                    .fetch();

            if (!images.isEmpty()) {
                post.addImage(images.stream().collect(Collectors.toSet()));
            }
        }
        long total = queryFactory
                .select(qPost.countDistinct())
                .from(qComment)
                .join(qComment.post, qPost)
                .where(qComment.member.id.eq(memberId))
                .fetchOne();

        return new PageImpl<>(posts, pageable, total);
    }

    public List<Comment> findCommentsById(Long id) {
        QComment qComment = QComment.comment;
        QMember qMember = QMember.member;

        return queryFactory
                .selectFrom(qComment)
                .leftJoin(qComment.member, qMember).fetchJoin()
                .where((qComment.groupName.eq(id.intValue())))
                .fetch();
    }

    public List<Comment> findCommentsWithReplyCountByPostId(Long postId) {
        QComment qComment = QComment.comment;

        return queryFactory
                .selectFrom(qComment)
                .where(qComment.post.id.eq(postId))
                .fetch();
    }
}
