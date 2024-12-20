package com.poten.dive_in.lesson.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.poten.dive_in.lesson.enums.LessonStatus;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class LessonRequestDto {

    @NotEmpty(message = "수업명은 필수입니다.")
    private String lessonName;

    private String level;

    private String capacity;

    private String price;

    private String keyword;

    private String lessonDetail;

    private String lessonSchedule;

    private LessonStatus lessonStatus;

    //    private Long academyId;
    private Long coachTeamId;

    private Long poolId;

    @JsonProperty("instructorIds")
    private List<Long> instructorIdList;

    @JsonProperty("applyChannels")
    private List<LessonApplyChannelDto> applyChannelDtoList;

//    public SwimClass toEntity() {
//
//        return SwimClass.builder()
//                .name(this.lessonName)
//                .participantCount(Integer.valueOf(this.capacity)) // capacity에서 변경
//                .price(Integer.valueOf(this.price)) // price
//                .introduction(this.lessonDetail) // lessonDetail에서 변경
//                .operatingHours(this.lessonSchedule) // lessonSchedule에서 변경
//                .lessonStatus(this.lessonStatus)
//                .build();
//    }

}
