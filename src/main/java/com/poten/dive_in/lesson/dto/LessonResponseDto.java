package com.poten.dive_in.lesson.dto;

import com.poten.dive_in.lesson.enums.LessonStatus;
import com.poten.dive_in.pool.entity.Pool;

public class LessonResponseDto {

    private String lessonName;

    private String level;

    private Integer capacity;

    private String price;

    private String keyword;

    private String lessonInfo;

    private String lessonDetail;

    private String lessonSchedule;

    private LessonStatus lessonStatus;

    private String academyId;

    private Pool poolId;
}
