package com.poten.dive_in.lesson.repository;

import com.poten.dive_in.lesson.entity.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LessonRepository extends JpaRepository<Lesson,Long> {
}
