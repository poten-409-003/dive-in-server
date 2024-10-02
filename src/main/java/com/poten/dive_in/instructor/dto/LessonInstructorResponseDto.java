package com.poten.dive_in.instructor.dto;

import com.poten.dive_in.instructor.entity.Instructor;
import com.poten.dive_in.lesson.entity.Lesson;
import com.poten.dive_in.lesson.entity.LessonInstructor;
import lombok.Builder;
import lombok.Getter;


@Getter @Builder
public class LessonInstructorResponseDto {

    private Long id;
    private Long instructorId;
    private String instructorName;
    private String instructorInfo;

    public static LessonInstructorResponseDto ofEntity(LessonInstructor lessonInstructor) {
        return LessonInstructorResponseDto.builder()
                .id(lessonInstructor.getId())
                .instructorId(lessonInstructor.getInstructor().getId())
                .instructorName(lessonInstructor.getInstructor().getInstructorName())
                .instructorInfo(lessonInstructor.getInstructor().getInstructorInfo())
                .build();
    }

}
