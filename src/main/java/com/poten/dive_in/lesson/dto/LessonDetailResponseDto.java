package com.poten.dive_in.lesson.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.poten.dive_in.academy.dto.AcademyResponseDto;
import com.poten.dive_in.instructor.dto.LessonInstructorResponseDto;
import com.poten.dive_in.lesson.entity.Lesson;
import com.poten.dive_in.lesson.enums.LessonStatus;
import com.poten.dive_in.pool.dto.PoolListResponseDto;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter @Builder
public class LessonDetailResponseDto {

    private Long id;

    private String lessonName;

    private String level;

    private Integer capacity;

    private String price;

    private String keyword;

    private String lessonInfo;

    private String lessonDetail;

    private String lessonSchedule;

    private LessonStatus lessonStatus;

    @JsonProperty("academy")
    private AcademyResponseDto academyResponseDto;

    @JsonProperty("pool")
    private PoolListResponseDto poolResponseDto;

    @JsonProperty("instructor")
    private List<LessonInstructorResponseDto> lessonInstructorResponseDtoList;

    @JsonProperty("image")
    private List<LessonImageDto> lessonImageDtoList;

    @JsonProperty("applyChannel")
    private List<LessonApplyChannelDto> lessonApplyChannelDtoList;

    public static LessonDetailResponseDto ofEntity(Lesson lesson) {

        // Lesson 이미지 리스트 처리
        List<LessonImageDto> lessonImageDtoList = (lesson.getImageList() != null) ?
                lesson.getImageList().stream()
                        .map(LessonImageDto::ofEntity)
                        .collect(Collectors.toList())
                : new ArrayList<>();

        // 강사 리스트 처리
        List<LessonInstructorResponseDto> lessonInstructorResponseDtoList = (lesson.getLessonInstructorList() != null) ?
                lesson.getLessonInstructorList().stream()
                        .map(LessonInstructorResponseDto::ofEntity)
                        .collect(Collectors.toList())
                : new ArrayList<>();

        // Apply Channel 리스트 처리
        List<LessonApplyChannelDto> lessonApplyChannelList = (lesson.getApplyChannelList() != null) ?
                lesson.getApplyChannelList().stream()
                        .map(LessonApplyChannelDto::ofEntity)
                        .toList() : new ArrayList<>();

        return LessonDetailResponseDto.builder()
                .id(lesson.getId())
                .lessonName(lesson.getLessonName())
                .level(lesson.getLevel() != null ? lesson.getLevel().name() : null)
                .capacity(lesson.getCapacity() != null ? lesson.getCapacity() : null)
                .price(lesson.getPrice() != null ? lesson.getPrice() : null)
                .keyword(lesson.getKeyword() != null ? lesson.getKeyword() : null)
                .lessonInfo(lesson.getLessonInfo() != null ? lesson.getLessonInfo() : null)
                .lessonDetail(lesson.getLessonDetail() != null ? lesson.getLessonDetail() : null)
                .lessonSchedule(lesson.getLessonSchedule() != null ? lesson.getLessonSchedule() : null)
                .lessonStatus(lesson.getLessonStatus() != null ? lesson.getLessonStatus() : null)
                .academyResponseDto(lesson.getAcademy() != null ? AcademyResponseDto.ofEntity(lesson.getAcademy()) : null)
                .poolResponseDto(lesson.getPool() != null ? PoolListResponseDto.ofEntity(lesson.getPool()) : null)
                .lessonInstructorResponseDtoList(lessonInstructorResponseDtoList)
                .lessonImageDtoList(lessonImageDtoList)
                .lessonApplyChannelDtoList(lessonApplyChannelList)
                .build();

    }
}
