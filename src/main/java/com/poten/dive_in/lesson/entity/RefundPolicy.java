package com.poten.dive_in.lesson.entity;

import com.poten.dive_in.common.entity.BaseTimeEntity;
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
@Table(name = "swmm_cls_rfnd") // DB 테이블명
public class RefundPolicy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rfnd_infr_id") // 환불 ID
    private Long refundId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cls_id") // 수업 ID (외래키)
    private SwimClass swimClass; // 관련 수업

    @Column(name = "rf_order") // 순서
    private String order;

    @Column(name = "dtls") // 환불 상세 설명
    private String details; // 상세 설명

    public void assignSwimClass(SwimClass swimClass) {
        this.swimClass = swimClass;
    }
}
