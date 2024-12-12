package com.poten.dive_in.lesson.entity;

import com.poten.dive_in.common.entity.BaseTimeEntity;
import com.poten.dive_in.instructor.entity.InstructorTeamMapping;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "swmm_cchn_team") // DB 테이블명
public class CoachingTeam extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cchn_team_id") // 팀 ID
    private Long id;

    @Column(name = "cchn_nm") // 코칭 팀 이름
    private String name;

    @Column(name = "cchn_expln") // 코칭 팀 설명
    private String description;

    @Column(name = "prfl_img_url") // 프로필 이미지 URL
    private String profileImageUrl;

    @Column(name = "use_yn") // 사용 여부
    private String isActive;

    @OneToMany(mappedBy = "swmmCchnTeam", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InstructorTeamMapping> instructorTeamMappings; // 강사 팀 매핑 리스트


}
