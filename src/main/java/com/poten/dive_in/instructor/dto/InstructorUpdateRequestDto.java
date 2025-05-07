package com.poten.dive_in.instructor.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


// 강사 프로필 수정 요청 DTO (기존 구조 유지)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InstructorUpdateRequestDto {

    // 수정 시 강사 ID는 PathVariable 등으로 받으므로 DTO에 포함하지 않음

    @NotBlank(message = "강사명은 필수입니다.")
    private String name; // 강사명

    private String isAthlete; // 선수출신여부
    private String awards; // 수상경력
    private String certifications; // 자격증
    private String description; // 설명
    // private String profileImageUrl; // 강사 프로필 이미지 URL (필요 시)

    // 소속 팀 정보 등 수정 시 함께 받을 정보 추가 (필요 시)
    // private List<Long> teamIds;
}
