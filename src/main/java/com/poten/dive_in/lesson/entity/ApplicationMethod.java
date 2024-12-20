package com.poten.dive_in.lesson.entity;

import com.poten.dive_in.cmmncode.entity.CommonCode;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "swmm_cls_appl_cd")
public class ApplicationMethod {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "appl_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cls_id")
    private SwimClass swimClass; // 수영 수업 ID

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appl_ty_cd", referencedColumnName = "cd")
    private CommonCode applicationType; // 신청 유형 코드

    @Column(name = "appl_url")
    private String applicationUrl; // 신청 URL

    public void assignSwimClass(SwimClass swimClass) {
        this.swimClass = swimClass;
    }

    public void assignApplicationType(CommonCode applicationType) {
        this.applicationType = applicationType;
    }
}
