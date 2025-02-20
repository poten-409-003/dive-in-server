// LessonRepositoryImpl.java
package com.poten.dive_in.lesson.repository;

import com.poten.dive_in.lesson.entity.QSwimClass;
import com.poten.dive_in.lesson.entity.SwimClass;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class LessonRepositoryCustomImpl implements LessonRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<SwimClass> findTopViewLessons() {
        QSwimClass qSwimClass = QSwimClass.swimClass;

        return queryFactory
                .selectFrom(qSwimClass)
                .where(qSwimClass.viewCount.gt(1))
                .orderBy(qSwimClass.viewCount.desc())
                .limit(4)
                .fetch();
    }

    @Override
    public List<SwimClass> findNewLessons() {
        QSwimClass qSwimClass = QSwimClass.swimClass;

        return queryFactory
                .selectFrom(qSwimClass)
                .orderBy(qSwimClass.createdAt.desc())
                .limit(4)
                .fetch();
    }
}