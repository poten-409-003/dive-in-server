package com.poten.dive_in.lesson.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.poten.dive_in.lesson.entity.ApplicationQualification;
import com.poten.dive_in.lesson.entity.LessonKeyword;
import com.poten.dive_in.lesson.entity.RefundPolicy;
import com.poten.dive_in.lesson.entity.SwimClass;
import com.poten.dive_in.pool.dto.PoolListResponseDto;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Comparator;
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

    private LessonDetailDto lessonDetail; // Changed type to LessonDetail object

    private String lessonSchedule;

    private String lessonStatus;

    private Integer viewCnt;

    @JsonProperty("academy")
    private CoachingTeamResponseDto coachingTeamResponseDto;

    @JsonProperty("pool")
    private PoolListResponseDto poolResponseDto;

//    @JsonProperty("instructors")
//    private List<LessonInstructorResponseDto> lessonInstructorResponseDtoList;

    @JsonProperty("images")
    private List<LessonImageDto> lessonImageDtoList;

//    @JsonProperty("applyChannels")
//    private List<LessonApplyChannelDto> lessonApplyChannelDtoList;

    @Getter
    @Builder
    public static class LessonDetailDto {
        private String topic;
        private List<String> eligibilityRequirements;
        private String introduction;
        private List<LessonApplyChannelDto> applicationMethod;
        private List<String> refundPolicy;
    }


    public static LessonDetailResponseDto ofEntity(SwimClass swimClass) {

        // Lesson 이미지 리스트 처리
        List<LessonImageDto> lessonImageDtoList = (swimClass.getImages() != null) ?
                swimClass.getImages().stream()
                        .map(LessonImageDto::ofEntity)
                        .collect(Collectors.toList())
                : new ArrayList<>();

        // 강사 리스트 처리
//        List<LessonInstructorResponseDto> lessonInstructorResponseDtoList = (swimClass.getInstructorTeam() != null &&
//                swimClass.getInstructorTeam().getInstructorTeamMappings() != null) ?
//                swimClass.getInstructorList() :
//                new ArrayList<>();

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
        if (lessonKeywords != null) { // Added null check for lessonKeywords
            for (LessonKeyword keyword : lessonKeywords) {
                if (keyword.getKeyword() != null) { // Added null check for keyword.getKeyword()
                    keywordList.add(keyword.getKeyword().getCodeName());
                }
            }
        }
        keywords = String.join(", ", keywordList);

        // Build the LessonDetail object with mappings from SwimClass entity
        List<String> eligibilityRequirements = (swimClass.getQualifications() != null) ?
                swimClass.getQualifications().stream()
                        .sorted(Comparator.comparing(ApplicationQualification::getOrder)) // Sort by order field
                        .map(ApplicationQualification::getDetails) // Mapping details from ApplicationQualification
                        .collect(Collectors.toList())
                : new ArrayList<>();

        List<String> refundPolicies = (swimClass.getRefunds() != null) ?
                swimClass.getRefunds().stream()
                        .sorted(Comparator.comparing(RefundPolicy::getOrder)) // Sort by order field
                        .map(RefundPolicy::getDetails) // Mapping details from RefundPolicy
                        .collect(Collectors.toList())
                : new ArrayList<>();


        // Reuse the already created lessonApplyChannelList for applicationMethod
        List<LessonApplyChannelDto> applicationMethods = lessonApplyChannelList;

        LessonDetailDto lessonDetailObject = LessonDetailDto.builder()
                .topic(swimClass.getSubject()) // Mapping subject to classTopic
                .eligibilityRequirements(eligibilityRequirements)
                .introduction(swimClass.getIntroduction()) // Mapping introduction to classIntroduction
                .applicationMethod(applicationMethods) // Using the list of LessonApplyChannelDto
                .refundPolicy(refundPolicies) // Using the list of refund policy details
                .build();


        return LessonDetailResponseDto.builder()
                .id(swimClass.getClassId())
                .lessonName(swimClass.getName())
                .level(level)
                .capacity(swimClass.getParticipantCount() != null ? String.valueOf(swimClass.getParticipantCount()) : null)
                .price(swimClass.getPrice() != null ? String.valueOf(swimClass.getPrice()) : "가격 문의")
                .keyword(keywords)
                .lessonDetail(lessonDetailObject) // Setting the LessonDetail object
                .lessonSchedule(swimClass.getOperatingHours() != null ? swimClass.getOperatingHours() : null)
                .lessonStatus(swimClass.getIsActive() != null ? swimClass.getIsActive() : null)
                .viewCnt(swimClass.getViewCount())
                .coachingTeamResponseDto(swimClass.getInstructorTeam() != null ? CoachingTeamResponseDto.ofEntity(swimClass.getInstructorTeam()) : null)
                .poolResponseDto(swimClass.getPool() != null ? PoolListResponseDto.ofEntity(swimClass.getPool()) : null)
                /*TODO instr_team_mpng 테이블 테스트 데이터 생성되면 수정*/
//                .lessonInstructorResponseDtoList(lessonInstructorResponseDtoList)
                .lessonImageDtoList(lessonImageDtoList)
//                .lessonApplyChannelDtoList(lessonApplyChannelList) // This line seems redundant now that applicationMethod is part of lessonDetail
                .build();
    }
}