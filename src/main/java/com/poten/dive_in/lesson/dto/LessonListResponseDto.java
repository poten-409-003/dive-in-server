package com.poten.dive_in.lesson.dto;

import com.poten.dive_in.lesson.entity.Lesson;
import lombok.Builder;
import lombok.Getter;

@Getter @Builder
public class LessonListResponseDto {
    private Long id;
    private String academyName;
    private String academyImageUrl;
    private String keyword;
    private String lessonName;
    private String level;
    private String price;

    public static LessonListResponseDto ofEntity(Lesson lesson) {
        return LessonListResponseDto.builder()
                .id(lesson.getId())
                .level(lesson.getLevel())
                .academyName(lesson.getAcademy() != null ? lesson.getAcademy().getAcademyName() : null)
                .academyImageUrl(lesson.getAcademy() != null ? lesson.getAcademy().getProfileImageUrl() : null)
                .keyword(lesson.getKeyword() != null ? lesson.getKeyword() : null)
                .lessonName(lesson.getLessonName() != null ? lesson.getLessonName() : null)
                .price(lesson.getPrice() != null ? lesson.getPrice() : null)
                .build();
        }
}
