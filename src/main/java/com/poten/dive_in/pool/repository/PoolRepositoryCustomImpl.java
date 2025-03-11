// LessonRepositoryImpl.java
package com.poten.dive_in.pool.repository;

import com.poten.dive_in.pool.entity.Pool;
import com.poten.dive_in.pool.entity.QPool;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class PoolRepositoryCustomImpl implements PoolRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Pool> findByKeyword(String keyword) {
        QPool pool = QPool.pool;
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(pool.poolName.contains(keyword));

        return queryFactory
                .selectFrom(pool)
                .where(builder)
                .orderBy(pool.createdAt.desc())
                .fetch();
    }
}