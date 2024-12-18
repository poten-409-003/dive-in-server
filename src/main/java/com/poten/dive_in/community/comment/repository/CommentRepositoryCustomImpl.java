package com.poten.dive_in.community.comment.repository;

import com.poten.dive_in.auth.entity.QMember;
import com.poten.dive_in.community.comment.entity.Comment;
import com.poten.dive_in.community.comment.entity.QComment;
import com.poten.dive_in.community.post.entity.Post;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class CommentRepositoryCustomImpl implements CommentRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    public Optional<Comment> findByIdWithMember(Long id) {
        QComment qComment = QComment.comment;
        QMember qMember = QMember.member;
        Comment result = queryFactory
                .selectFrom(qComment)
                .join(qComment.member, qMember).fetchJoin() // fetch join을 사용하여 Member를 함께 로드
                .where(qComment.id.eq(id))
                .fetchOne();

        return Optional.ofNullable(result);
    }

    public Page<Post> findDistinctPostsByMemberId(Long memberId, Pageable pageable) {
        QComment qComment = QComment.comment;

        // 중복을 제거한 게시글 목록을 가져오기 위한 쿼리
        List<Post> posts = queryFactory
                .select(qComment.post)
                .distinct() // 중복 제거
                .from(qComment)
                .where(qComment.member.id.eq(memberId)) // 특정 회원의 댓글 필터링
                .orderBy(qComment.createdAt.desc()) // 최신 등록일자 기준 정렬
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // 전체 게시글 수를 가져오기 위한 쿼리
        long total = queryFactory
                .select(qComment.post.countDistinct()) // 중복 제거된 총 개수
                .from(qComment)
                .where(qComment.member.id.eq(memberId))
                .stream().count();

        return new PageImpl<>(posts, pageable, total);
    }
}
