package com.poten.dive_in.instructor.entity;

import com.poten.dive_in.auth.entity.Member;
import com.poten.dive_in.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "swmm_instr") // DB 테이블명
public class Instructor extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "instr_id") // 강사 ID
    private Long id;

    @OneToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(name = "Field") // 강사명
    private String name;

    @Column(name = "Field2") // 선수출신여부
    private String isAthlete;

    @Column(name = "Field3") // 수상경력
    private String awards;

    @Column(name = "Field4") // 자격증
    private String certifications;

    @Column(name = "Field5") // 설명
    private String description;


    @Column(name = "use_yn") // 사용 여부
    private String isActive;

    @OneToMany(mappedBy = "instructor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InstructorTeamMapping> teamMappings; // 강사와 팀 매핑 리스트

}
