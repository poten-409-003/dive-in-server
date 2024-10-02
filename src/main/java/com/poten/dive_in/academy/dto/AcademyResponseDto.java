package com.poten.dive_in.academy.dto;

import com.poten.dive_in.academy.entity.Academy;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AcademyResponseDto {

    private Long id;

    private String academyName;

    private String academyInfo;

    private String profileImageUrl;

    public static AcademyResponseDto ofEntity(Academy academy){
        return AcademyResponseDto.builder()
                .id(academy.getId())
                .academyInfo(academy.getAcademyInfo())
                .academyName(academy.getAcademyName())
                .profileImageUrl(academy.getProfileImageUrl())
                .build();
    }
}
