package com.poten.dive_in.instructor.dto;

import com.poten.dive_in.instructor.entity.Instructor;
import lombok.Builder;
import lombok.Getter;

// 강사 프로필 응답 DTO (LessonInstructorResponseDto 기반 재구성 - 목록용)
@Getter
@Builder
public class InstructorProfileResponseDto {

    private Long id; // 강사 ID
    private String instructorName; // 강사명 (기존 필드명 유지)
    private String instructorInfo; // 강사 설명 (기존 필드명 유지)
    // private String profileImageUrl; // 강사 프로필 이미지 URL (필요 시 추가)

    // Entity를 DTO로 변환하는 팩토리 메서드
    public static InstructorProfileResponseDto ofEntity(Instructor instructor) {
        return InstructorProfileResponseDto.builder()
                .id(instructor.getId())
                .instructorName(instructor.getName())
                .instructorInfo(instructor.getDescription())
                // .profileImageUrl(...) // 필드 추가 시 매핑
                .build();
    }
}

