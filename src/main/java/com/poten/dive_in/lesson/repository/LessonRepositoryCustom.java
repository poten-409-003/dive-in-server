package com.poten.dive_in.lesson.repository;

import com.poten.dive_in.instructor.entity.Instructor;
import com.poten.dive_in.lesson.dto.SwimClassSearchCondition;
import com.poten.dive_in.lesson.entity.SwimClass;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface LessonRepositoryCustom {
    List<SwimClass> findTopViewLessons();

    List<SwimClass> findNewLessons();

    List<SwimClass> findByKeyword(String keyword);

    /**
     * 특정 수영 클래스에 연결된 강사 목록을 조회합니다.
     *
     * @param classId 수영 클래스 ID
     * @return 해당 클래스의 강사 목록
     */
    List<Instructor> findInstructorsByClassId(Long classId);

    /**
     * 수영 클래스 목록을 페이징, 검색, 필터링하여 조회합니다.
     *
     * @param condition 검색/필터링 조건 DTO
     * @param pageable  페이징 정보
     * @return 수영 클래스 목록 (Page)
     */
    Page<SwimClass> findSwimClasses(SwimClassSearchCondition condition, Pageable pageable);

}