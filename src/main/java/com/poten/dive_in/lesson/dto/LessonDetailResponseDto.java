package com.poten.dive_in.lesson.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.poten.dive_in.instructor.dto.LessonInstructorResponseDto;
import com.poten.dive_in.lesson.entity.LessonKeyword;
import com.poten.dive_in.lesson.entity.SwimClass;
import com.poten.dive_in.pool.dto.PoolListResponseDto;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.poten.dive_in.lesson.service.LessonUtils.getLevelsByCode;

@Getter
@Builder
@ToString
public class LessonDetailResponseDto {

    private Long id;

    private String lessonName;

    private String level;

    private String capacity;

    private String price;

    private String keyword;

    private String lessonDetail;

    private String lessonSchedule;

    private String lessonStatus;

    @JsonProperty("academy")
    private CoachingTeamResponseDto coachingTeamResponseDto;

    @JsonProperty("pool")
    private PoolListResponseDto poolResponseDto;

    @JsonProperty("instructors")
    private List<LessonInstructorResponseDto> lessonInstructorResponseDtoList;

    @JsonProperty("images")
    private List<LessonImageDto> lessonImageDtoList;

    @JsonProperty("applyChannels")
    private List<LessonApplyChannelDto> lessonApplyChannelDtoList;

    public static LessonDetailResponseDto ofEntity(SwimClass swimClass) {

        // Lesson 이미지 리스트 처리
        List<LessonImageDto> lessonImageDtoList = (swimClass.getImages() != null) ?
                swimClass.getImages().stream()
                        .map(LessonImageDto::ofEntity)
                        .collect(Collectors.toList())
                : new ArrayList<>();

        // 강사 리스트 처리
        List<LessonInstructorResponseDto> lessonInstructorResponseDtoList = (swimClass.getInstructorTeam() != null &&
                swimClass.getInstructorTeam().getInstructorTeamMappings() != null) ?
                swimClass.getInstructorList() :
                new ArrayList<>();

        // Apply Channel 리스트 처리
        List<LessonApplyChannelDto> lessonApplyChannelList = (swimClass.getApplicationMethods() != null) ?
                swimClass.getApplicationMethods().stream()
                        .map(LessonApplyChannelDto::ofEntity)
                        .toList() : new ArrayList<>();

        String level = null;
        if (swimClass.getLevel() != null) {
            level = getLevelsByCode(swimClass.getLevel());
        }

        Set<LessonKeyword> lessonKeywords = swimClass.getKeywords();
        String keywords = null;
        List<String> keywordList = new ArrayList<>();
        for (LessonKeyword keyword : lessonKeywords) {
            keywordList.add(keyword.getKeyword().getCodeName());
        }
        keywords = String.join(", ", keywordList);

        return LessonDetailResponseDto.builder()
                .id(swimClass.getClassId())
                .lessonName(swimClass.getName())
                .level(level)
                .capacity(swimClass.getParticipantCount() != null ? String.valueOf(swimClass.getParticipantCount()) : null)
                .price(swimClass.getPrice() != null ? String.valueOf(swimClass.getPrice()) : null)
                .keyword(keywords)
                .lessonDetail(swimClass.getIntroduction() != null ? swimClass.getIntroduction() : null)
                .lessonSchedule(swimClass.getOperatingHours() != null ? swimClass.getOperatingHours() : null)
                .lessonStatus(swimClass.getIsActive() != null ? swimClass.getIsActive() : null)
                .coachingTeamResponseDto(swimClass.getInstructorTeam() != null ? CoachingTeamResponseDto.ofEntity(swimClass.getInstructorTeam()) : null)
                .poolResponseDto(swimClass.getPool() != null ? PoolListResponseDto.ofEntity(swimClass.getPool()) : null)
/*TODO instr_team_mpng 테이블 테스트 데이터 생성되면 수정*/
                .lessonInstructorResponseDtoList(lessonInstructorResponseDtoList)
                .lessonImageDtoList(lessonImageDtoList)
                .lessonApplyChannelDtoList(lessonApplyChannelList)
                .build();
    }
}
