package com.poten.dive_in.instructor.entity;

import com.poten.dive_in.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter @Builder
@AllArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name = "swmm_cchn_team")
public class InstrTeamMpng extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cchn_team_id")
    private Long id;

    private String cchnNm;
    private String cchnExpln;
    private String prflImgUrl;
    private String useYn;
}
