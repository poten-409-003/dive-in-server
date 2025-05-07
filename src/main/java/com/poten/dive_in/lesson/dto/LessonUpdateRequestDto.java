package com.poten.dive_in.lesson.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

// 수영 클래스 수정 요청 DTO (LessonRequestDto 및 SwimClassUpdateRequestDto 기반 재구성)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LessonUpdateRequestDto {

    // 수정 시 클래스 ID는 PathVariable 등으로 받으므로 DTO에 포함하지 않음

    @NotBlank(message = "수업명은 필수입니다.")
    private String lessonName; // 수업 이름

    @NotBlank(message = "수업 레벨은 필수입니다.")
    private String level; // 수업 레벨 (CommonCode CD 값)

    @NotNull(message = "인원수는 필수입니다.")
    @Min(value = 1, message = "인원수는 1명 이상이어야 합니다.")
    private Integer capacity; // 인원수

    @NotNull(message = "가격은 필수입니다.")
    @Min(value = 0, message = "가격은 0 이상이어야 합니다.")
    private Integer price; // 가격

    @NotBlank(message = "수업 소개는 필수입니다.")
    private String lessonDetail; // 수업 소개

    @NotBlank(message = "운영 시간은 필수입니다.")
    private String lessonSchedule; // 운영시간

    // LessonStatus 상태 변경 기능이 필요하다면 추가
    // private LessonStatus lessonStatus;

    @NotNull(message = "코칭 팀 ID는 필수입니다.")
    private Long coachTeamId; // 수영 코칭 팀 ID

    @NotNull(message = "수영장 ID는 필수입니다.")
    private Long poolId; // 수영장 ID

    // 연관 엔티티 정보 (수정 시 함께 업데이트할 정보) - 기존 DTO 구조 및 엔티티 기반
    // 수정 시에는 기존 데이터의 ID를 포함하여 수정 또는 삭제를 구분할 수 있도록 설계 필요
    private List<LessonImageUpdateRequestDto> images; // 이미지 목록 (수정용 DTO 별도 정의)
    private List<ApplicationQualificationUpdateRequestDto> qualifications; // 수업 자격 리스트 (수정용 DTO 별도 정의)
    private List<RefundPolicyUpdateRequestDto> refunds; // 환불 리스트 (수정용 DTO 별도 정의)
    private List<LessonKeywordUpdateRequestDto> keywords; // 키워드 리스트 (수정용 DTO 별도 정의)
    private List<LessonApplyChannelUpdateRequestDto> applyChannels; // 신청 방법 리스트 (수정용 DTO 별도 정의)

    // Inner DTOs for related entities (Update) - 기존 구조 및 엔티티 기반
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LessonImageUpdateRequestDto {
        private Long id; // 수정/삭제 시 필요
        @NotBlank(message = "이미지 URL은 필수입니다.")
        private String imageUrl; // 이미지 URL
        private boolean isRepresentative; // 대표 이미지 여부
        // 삭제를 위한 필드 추가 가능 (예: boolean delete)
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ApplicationQualificationUpdateRequestDto {
        private Long id; // 수정/삭제 시 필요
        @NotBlank(message = "자격 상세 설명은 필수입니다.")
        private String details; // 상세 설명
        private Integer order; // 순서
        // 삭제를 위한 필드 추가 가능
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RefundPolicyUpdateRequestDto {
        private Long id; // 수정/삭제 시 필요
        @NotBlank(message = "환불 상세 설명은 필수입니다.")
        private String details; // 상세 설명
        private Integer order; // 순서
        // 삭제를 위한 필드 추가 가능
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LessonKeywordUpdateRequestDto {
        private Long id; // 수정/삭제 시 필요
        @NotBlank(message = "키워드 코드는 필수입니다.")
        private String keywordCode; // 키워드 코드 (CommonCode CD 값)
        // 삭제를 위한 필드 추가 가능
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LessonApplyChannelUpdateRequestDto {
        private Long id; // 수정/삭제 시 필요
        @NotBlank(message = "신청 유형 코드는 필수입니다.")
        private String applicationTypeCode; // 신청 유형 코드 (CommonCode CD 값)
        @NotBlank(message = "신청 URL은 필수입니다.")
        private String applicationUrl; // 신청 URL
        // 삭제를 위한 필드 추가 가능
    }
}
