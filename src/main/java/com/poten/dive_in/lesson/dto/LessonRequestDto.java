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

// 수영 클래스 등록 요청 DTO (LessonRequestDto 및 SwimClassCreateRequestDto 기반 재구성)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LessonRequestDto {

    @NotBlank(message = "수업명은 필수입니다.")
    private String lessonName; // 수업 이름 (LessonRequestDto 필드명 사용)

    @NotBlank(message = "수업 레벨은 필수입니다.")
    private String level; // 수업 레벨 (CommonCode CD 값)

    @NotNull(message = "인원수는 필수입니다.")
    @Min(value = 1, message = "인원 수는 1명 이상이어야 합니다.")
    private Integer capacity; // 인원수 (LessonRequestDto 필드명 사용, 타입 변경)

    @NotNull(message = "가격은 필수입니다.")
    @Min(value = 0, message = "가격은 0 이상이어야 합니다.")
    private Integer price; // 가격 (LessonRequestDto 필드명 사용, 타입 변경)

    @NotNull(message = "주제는 필수입니다.")
    @Min(value = 0, message = "주제는 2자 이상이어야 합니다.")
    private String subject; // 가격 (LessonRequestDto 필드명 사용, 타입 변경)

    @NotBlank(message = "수업 소개는 필수입니다.")
    private String lessonIntroduction; // 수업 소개 (LessonRequestDto 필드명 사용)

    @NotBlank(message = "운영 시간은 필수입니다.")
    private String lessonSchedule; // 운영시간 (LessonRequestDto 필드명 사용)

    // LessonStatus는 등록 시 기본값 설정 또는 별도 필드로 받을 수 있음
    // private LessonStatus lessonStatus;

    @NotNull(message = "코칭 팀 ID는 필수입니다.")
    private Long coachTeamId; // 수영 코칭 팀 ID (LessonRequestDto 필드명 사용)

    @NotNull(message = "수영장 ID는 필수입니다.")
    private Long poolId; // 수영장 ID

    // 연관 엔티티 정보 (등록 시 함께 받을 수 있는 정보) - 기존 DTO 구조 및 엔티티 기반
    private List<LessonImageRequestDto> images; // 이미지 목록 (요청용 DTO 별도 정의)
    private List<ApplicationQualificationDto> qualifications; // 수업 자격 리스트
    private List<RefundPolicyDto> refunds; // 환불 리스트
    private List<LessonKeywordRequestDto> keywords; // 키워드 리스트 (요청용 DTO 별도 정의)
    private List<LessonApplyChannelRequestDto> applyChannels; // 신청 방법 리스트 (요청용 DTO 별도 정의)

    // Inner DTOs for related entities (Request) - 기존 구조 및 엔티티 기반
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LessonImageRequestDto {
        // 등록 시에는 ID 필요 없음
        @NotBlank(message = "이미지 URL은 필수입니다.")
        private String imageUrl; // 이미지 URL
        private boolean isRepresentative; // 대표 이미지 여부
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ApplicationQualificationDto {
        // 등록 시에는 ID 필요 없음
        @NotBlank(message = "자격 상세 설명은 필수입니다.")
        private String details; // 상세 설명
        private Integer order; // 순서 (엔티티는 String이지만 DTO에서는 Integer로 받는 것이 편리)
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RefundPolicyDto {
        // 등록 시에는 ID 필요 없음
        @NotBlank(message = "환불 상세 설명은 필수입니다.")
        private String details; // 상세 설명
        private Integer order; // 순서 (엔티티는 String이지만 DTO에서는 Integer로 받는 것이 편리)
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LessonKeywordRequestDto {
        // 등록 시에는 ID 필요 없음
        @NotBlank(message = "키워드 코드는 필수입니다.")
        private String keywordCode; // 키워드 코드 (CommonCode CD 값)
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LessonApplyChannelRequestDto {
        // 등록 시에는 ID 필요 없음
        @NotBlank(message = "신청 유형 코드는 필수입니다.")
        private String applicationTypeCode; // 신청 유형 코드 (CommonCode CD 값)
        @NotBlank(message = "신청 URL은 필수입니다.")
        private String applicationUrl; // 신청 URL
    }
}
