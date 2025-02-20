package com.poten.dive_in.lesson.repository;

import com.poten.dive_in.lesson.entity.SwimClass;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LessonRepository extends JpaRepository<SwimClass, Long>, LessonRepositoryCustom {

    @EntityGraph(value = "SwimClass.detail", type = EntityGraph.EntityGraphType.LOAD)
    List<SwimClass> findAll();

    @EntityGraph(value = "SwimClass.detail", type = EntityGraph.EntityGraphType.LOAD)
    Optional<SwimClass> findById(Long id);

    List<SwimClass> findByNameContainingOrKeywordsKeywordCodeNameContaining(String name, String keyword);


}
