package com.poten.dive_in.cmmncode.entity;

import com.poten.dive_in.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Table(name="CmmnCd")
public class CommonCode extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cd_id")
    private Long id;

    @Column(name = "cd_grp_nm")
    private String grpNm;

    @Column(name = "cd_grp")
    private String grp;

    @Column(name = "cd")
    private String code;

    @Column(name = "cd_nm")
    private String codeName;

    @Column(name = "up_cd_grp")
    private String parentGroupCode;
    @Column(name = "up_cd")
    private String parentCode;

    private Character useYn;

}
