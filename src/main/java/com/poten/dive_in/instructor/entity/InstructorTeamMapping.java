package com.poten.dive_in.instructor.entity;

import com.poten.dive_in.lesson.entity.CoachingTeam;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter @Builder
@AllArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name = "instr_team_mpng")
public class InstructorTeamMapping {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mpng_id")
    private Long mappingId;

    @ManyToOne(fetch = FetchType.LAZY) // 다대일 관계 설정
    @JoinColumn(name = "cchn_team", referencedColumnName = "cchn_team_id") // 외래키 컬럼명
    private CoachingTeam swmmCchnTeam; // 코칭 팀 정보


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "instr_id")
    private Instructor instructor; // 코치 정보

    @Column(name = "tm_ledr_yn")
    private String isLeader; // 팀 리더 여부

}
