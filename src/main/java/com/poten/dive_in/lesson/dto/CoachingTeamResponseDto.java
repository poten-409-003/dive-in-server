package com.poten.dive_in.lesson.dto;

import com.poten.dive_in.lesson.entity.CoachingTeam;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CoachingTeamResponseDto {

    private Long id;

    private String academyName;

    private String academyInfo;

    private String profileImageUrl;

    /* TODO 수정 필요 */
    public static CoachingTeamResponseDto ofEntity(CoachingTeam coachingTeam) {
        return CoachingTeamResponseDto.builder()
                .id(coachingTeam.getId())
                .academyInfo(coachingTeam.getDescription())
                .academyName(coachingTeam.getName())
                .profileImageUrl(coachingTeam.getProfileImageUrl())
                .build();
    }
}
