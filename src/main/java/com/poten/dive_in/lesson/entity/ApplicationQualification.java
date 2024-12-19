package com.poten.dive_in.lesson.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "swmm_cls_aply_qlfc") // DB 테이블명
public class ApplicationQualification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "aply_qlfc_id") // 자격 ID
    private Long qualificationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cls_id") // 수업 ID (외래키)
    private SwimClass swimClass; // 관련 수업

    @Column(name = "dt_order") // 순서
    private String order;

    @Column(name = "dtls") // 자격 상세 설명
    private String details; // 상세 설명

    public void assignSwimClass(SwimClass swimClass) {
        this.swimClass = swimClass;
    }
}
