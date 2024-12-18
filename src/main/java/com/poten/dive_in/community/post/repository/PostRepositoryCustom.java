package com.poten.dive_in.community.post.repository;

import com.poten.dive_in.community.post.entity.Post;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import com.poten.dive_in.community.post.entity.QPost;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class PostRepositoryCustom implements PostRepositoryImpl {

    private final JPAQueryFactory queryFactory;

    public List<Post> findActivePosts() {
        QPost qPost = QPost.post;
        return queryFactory.selectFrom(qPost)
                .where(qPost.isActive.eq("Y"))
                .orderBy(qPost.createdAt.desc())
                .fetch();
    }

    public List<Post> findPopularPosts() {
        QPost qPost = QPost.post;
        return queryFactory.selectFrom(qPost)
                .where(qPost.viewCount.goe(10))
                .orderBy(qPost.createdAt.desc())
                .fetch();
    }

    public List<Post> findPostsByMemberId(Long memberId) {
        QPost qPost = QPost.post;
        return queryFactory.selectFrom(qPost)
                .where(qPost.member.id.eq(memberId))
                .orderBy(qPost.createdAt.desc())
                .fetch();
    }
}

