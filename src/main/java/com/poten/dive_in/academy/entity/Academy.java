package com.poten.dive_in.academy.entity;

import com.poten.dive_in.academy.dto.AcademyRequestDto;
import com.poten.dive_in.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@Entity
public class Academy extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "academy_id")
    private Long id;

    private String academyName;

    private String academyInfo;

    private String profileImageUrl;

    public void updateAcademy(AcademyRequestDto academyRequestDto, String profileImageUrl){
        this.academyName = academyRequestDto.getAcademyName();
        this.academyInfo = academyRequestDto.getAcademyInfo();
        this.profileImageUrl = profileImageUrl;
    }
}
