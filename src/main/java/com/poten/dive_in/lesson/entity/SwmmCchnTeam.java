package com.poten.dive_in.lesson.entity;

import com.poten.dive_in.common.entity.BaseTimeEntity;
import com.poten.dive_in.instructor.entity.InstrTeamMpng;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter @Setter
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "swmm_cchn_team")
public class SwmmCchnTeam extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer cchnTeamId;

    private String cchnNm;
    private String cchnExpln;
    private String prflImgUrl;
    private String useYn;

    @OneToMany(mappedBy = "swmmCchnTeam")
    private List<InstrTeamMpng> instrTeamMpngList;

}

