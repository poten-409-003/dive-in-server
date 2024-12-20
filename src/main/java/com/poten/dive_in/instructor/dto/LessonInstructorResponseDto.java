package com.poten.dive_in.instructor.dto;

import com.poten.dive_in.instructor.entity.Instructor;
import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
public class LessonInstructorResponseDto {

    private Long id;
    private Long instructorId;
    private String instructorName;
    private String instructorInfo;

    public static LessonInstructorResponseDto ofEntity(Instructor instructor) {
        return LessonInstructorResponseDto.builder()
                .id(instructor.getId())
                .instructorId(instructor.getId())
                .instructorName(instructor.getName())
                .instructorInfo(instructor.getDescription())
                .build();
    }

}
