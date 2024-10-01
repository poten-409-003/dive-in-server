package com.poten.dive_in.lesson.entity;

import com.poten.dive_in.academy.entity.Academy;
import com.poten.dive_in.common.entity.BaseTimeEntity;
import com.poten.dive_in.lesson.enums.LessonStatus;
import com.poten.dive_in.pool.entity.Pool;
import jakarta.persistence.*;

@Entity
public class Lesson extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lesson_id")
    private Long id;

    private String lessonName;

    private String level;

    private Integer capacity;

    private String price;

    private String keyword;

    private String lessonInfo;

    private String lessonDetail;

    private String lessonSchedule;

    @Enumerated(EnumType.STRING)
    private LessonStatus lessonStatus;

    @ManyToOne
    @JoinColumn(name = "academy_id")
    private Academy academy;

    @ManyToOne
    @JoinColumn(name = "pool_id")
    private Pool pool;

}
