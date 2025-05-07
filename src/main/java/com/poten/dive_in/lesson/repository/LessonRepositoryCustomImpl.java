// LessonRepositoryImpl.java
package com.poten.dive_in.lesson.repository;

import com.poten.dive_in.instructor.entity.Instructor;
import com.poten.dive_in.instructor.entity.QInstructor;
import com.poten.dive_in.instructor.entity.QInstructorTeamMapping;
import com.poten.dive_in.lesson.dto.SwimClassSearchCondition;
import com.poten.dive_in.lesson.entity.QCoachingTeam;
import com.poten.dive_in.lesson.entity.QLessonKeyword;
import com.poten.dive_in.lesson.entity.QSwimClass;
import com.poten.dive_in.lesson.entity.SwimClass;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;

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

    @Override
    public List<SwimClass> findByKeyword(String keyword) {
        QSwimClass swimClass = QSwimClass.swimClass;
        QLessonKeyword lessonKeyword = QLessonKeyword.lessonKeyword;
        BooleanBuilder builder = new BooleanBuilder();
        builder.or(swimClass.name.contains(keyword))
                .or(swimClass.keywords.any().keyword.codeName.contains(keyword));

        return queryFactory
                .selectFrom(swimClass)
                .leftJoin(swimClass.keywords, lessonKeyword).fetchJoin()
                .where(builder)
                .orderBy(swimClass.createdAt.desc())
                .fetch();
    }

    /**
     * 특정 수영 클래스에 연결된 강사 목록을 조회합니다.
     * SwimClass -> CoachingTeam -> InstructorTeamMapping -> Instructor 경로를 따라 조인합니다.
     *
     * @param classId 수영 클래스 ID
     * @return 해당 클래스의 강사 목록
     */
    @Override
    public List<Instructor> findInstructorsByClassId(Long classId) {
        QSwimClass swimClass = QSwimClass.swimClass;
        QCoachingTeam coachingTeam = QCoachingTeam.coachingTeam;
        QInstructorTeamMapping instructorTeamMapping = QInstructorTeamMapping.instructorTeamMapping;
        QInstructor instructor = QInstructor.instructor;

        return queryFactory
                .select(instructor) // 강사 엔티티 선택
                .from(swimClass)
                .join(swimClass.instructorTeam, coachingTeam) // SwimClass와 CoachingTeam 조인
                .join(coachingTeam.instructorTeamMappings, instructorTeamMapping) // CoachingTeam과 InstructorTeamMapping 조인
                .join(instructorTeamMapping.instructor, instructor) // InstructorTeamMapping과 Instructor 조인
                .where(swimClass.classId.eq(classId)) // 특정 클래스 ID 조건
                .fetch(); // 결과 목록 조회
    }

    /**
     * 수영 클래스 목록을 페이징, 검색, 필터링하여 조회합니다.
     * Querydsl을 활용하여 동적인 조건을 처리합니다.
     *
     * @param condition 검색/필터링 조건 DTO
     * @param pageable  페이징 정보
     * @return 수영 클래스 목록 (Page)
     */
    @Override
    public Page<SwimClass> findSwimClasses(SwimClassSearchCondition condition, Pageable pageable) {
        QSwimClass swimClass = QSwimClass.swimClass;

        JPQLQuery<SwimClass> query = queryFactory
                .selectFrom(swimClass)
                .where(isClassActive(), // 활성화된 클래스만 조회
                        searchCondition(condition)); // 검색/필터링 조건 적용

        // 페이징 적용
        List<SwimClass> content = query
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(swimClass.createdAt.desc()) // 기본 정렬: 최신순
                // .orderBy(...) // 필요에 따라 다른 정렬 조건 추가 가능 (예: condition.getSort())
                .fetch();

        // 전체 카운트 (성능을 위해 별도 쿼리 사용)
        long total = queryFactory
                .selectFrom(swimClass)
                .where(isClassActive(),
                        searchCondition(condition))
                .fetchCount();

        return new PageImpl<>(content, pageable, total);
    }

    /**
     * 활성화된 클래스만 조회하는 조건
     */
    private BooleanExpression isClassActive() {
        QSwimClass swimClass = QSwimClass.swimClass;
        return swimClass.isActive.eq("Y");
    }

    /**
     * 검색 및 필터링 조건을 동적으로 구성하는 메서드
     *
     * @param condition 검색/필터링 조건 DTO
     * @return BooleanExpression (조합된 조건)
     */
    private BooleanBuilder searchCondition(SwimClassSearchCondition condition) {
        BooleanBuilder builder = new BooleanBuilder();
        QSwimClass swimClass = QSwimClass.swimClass;

        // 예시: 클래스 이름 검색 조건
        if (StringUtils.hasText(condition.getClassName())) {
            builder.and(swimClass.name.contains(condition.getClassName()));
        }

        // 예시: 레벨 필터링 조건
        if (StringUtils.hasText(condition.getLevel())) {
            builder.and(swimClass.level.eq(condition.getLevel()));
        }

        // 예시: 가격 범위 필터링 조건
        if (condition.getMinPrice() != null) {
            builder.and(swimClass.price.goe(condition.getMinPrice()));
        }
        if (condition.getMaxPrice() != null) {
            builder.and(swimClass.price.loe(condition.getMaxPrice()));
        }

        // ... 다른 검색/필터링 조건 추가 ...

        return builder;
    }


}