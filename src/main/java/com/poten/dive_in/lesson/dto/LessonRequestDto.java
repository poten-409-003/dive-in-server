package com.poten.dive_in.lesson.dto;

import com.poten.dive_in.lesson.entity.Lesson;
import com.poten.dive_in.lesson.enums.LessonLevel;
import com.poten.dive_in.lesson.enums.LessonStatus;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter @Builder
public class LessonRequestDto {

    @NotEmpty(message = "수업명은 필수입니다.")
    private String lessonName;

    private LessonLevel level;

    private Integer capacity;

    private String price;

    private String keyword;

    private String lessonInfo;

    private String lessonDetail;

    private String lessonSchedule;

    private LessonStatus lessonStatus;

    private Long academyId;

    private Long poolId;

    private List<Long> instructorIdList;

    private List<LessonApplyChannelDto> applyChannelDtoList;

    public Lesson toEntity() {
        return Lesson.builder()
                .lessonName(this.lessonName)
                .level(this.level)
                .capacity(this.capacity)
                .price(this.price)
                .keyword(this.keyword)
                .lessonInfo(this.lessonInfo)
                .lessonDetail(this.lessonDetail)
                .lessonSchedule(this.lessonSchedule)
                .lessonStatus(this.lessonStatus)
                .build();
    }


}
