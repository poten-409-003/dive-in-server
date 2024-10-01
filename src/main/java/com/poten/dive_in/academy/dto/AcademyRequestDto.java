package com.poten.dive_in.academy.dto;

import com.poten.dive_in.academy.entity.Academy;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class AcademyRequestDto {

    @NotEmpty(message = "업체명을 입력해주세요.")
    private String academyName;

    private String academyInfo;

    public Academy toEntity(String profileImageUrl ){
        return Academy.builder()
                .academyName(this.academyName)
                .academyInfo(this.academyInfo)
                .profileImageUrl(profileImageUrl)
                .build();
    }

}
