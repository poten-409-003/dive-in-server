package com.poten.dive_in.lesson.repository;

import com.poten.dive_in.lesson.entity.SwimClass;

import java.util.List;

public interface LessonRepositoryCustom {
    List<SwimClass> findTopViewLessons();

    List<SwimClass> findNewLessons();
}