package com.poten.dive_in.instructor.dto;

import com.poten.dive_in.instructor.entity.Instructor;
import com.poten.dive_in.lesson.dto.CoachingTeamResponseDto; // 코칭 팀 응답 DTO 필요
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

// 강사 프로필 상세 조회 응답 DTO (새롭게 정의)
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InstructorProfileDetailResponseDto {

    private Long instructorId; // 강사 ID
    private String name; // 강사명
    private String isAthlete; // 선수출신여부
    private String awards; // 수상경력
    private String certifications; // 자격증
    private String description; // 설명
    // private String profileImageUrl; // 강사 프로필 이미지 URL (필요 시 추가)

    // 소속 팀 정보 목록
    private List<CoachingTeamResponseDto> coachingTeams;

    // Entity를 DTO로 변환하는 팩토리 메서드
    public static InstructorProfileDetailResponseDto ofEntity(Instructor instructor) {
        // 소속 팀 정보 매핑 (InstructorTeamMapping 통해 가져옴)
        List<CoachingTeamResponseDto> coachingTeamDtos = (instructor.getTeamMappings() != null) ?
                instructor.getTeamMappings().stream()
                        .filter(mapping -> mapping.getSwmmCchnTeam() != null) // 코칭 팀 정보가 있는 매핑만 필터링
                        .map(mapping -> CoachingTeamResponseDto.ofEntity(mapping.getSwmmCchnTeam())) // 코칭 팀 엔티티를 DTO로 변환
                        .collect(Collectors.toList()) : null;

        return InstructorProfileDetailResponseDto.builder()
                .instructorId(instructor.getId())
                .name(instructor.getName())
                .isAthlete(instructor.getIsAthlete())
                .awards(instructor.getAwards())
                .certifications(instructor.getCertifications())
                .description(instructor.getDescription())
                // .profileImageUrl(...) // 필드 추가 시 매핑
                .coachingTeams(coachingTeamDtos)
                .build();
    }
}

