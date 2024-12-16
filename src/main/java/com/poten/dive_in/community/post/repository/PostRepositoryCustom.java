package com.poten.dive_in.community.post.repository;

import com.poten.dive_in.community.post.entity.Post;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.poten.dive_in.community.post.entity.QPost.post;

@Repository
@RequiredArgsConstructor
public class PostRepositoryCustom implements PostRepositoryImpl {

    private final JPAQueryFactory queryFactory;

    public List<Post> findActivePosts() {
        return queryFactory.selectFrom(post)
                .where(post.isActive.eq("Y"))
                .orderBy(post.createdAt.desc())
                .fetch();
    }

    public List<Post> findPopularPosts() {
        return queryFactory.selectFrom(post)
                .where(post.viewCount.goe(10))
                .orderBy(post.createdAt.desc())
                .fetch();
    }

    public List<Post> findPostsByMemberId(Long memberId) {
        return queryFactory.selectFrom(post)
                .where(post.member.id.eq(memberId))
                .orderBy(post.createdAt.desc())
                .fetch();
    }
}

