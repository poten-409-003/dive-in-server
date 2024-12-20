package com.poten.dive_in.lesson.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Builder
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "swmm_cls_img")
public class SwimClassImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "img_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cls_id")
    private SwimClass swimClass; // 수영 수업 ID

    @Column(name = "rprs_img_yn")
    private String isRepresentative; // 대표 이미지 여부

    @Column(name = "img_url")
    private String imageUrl; // 이미지 URL

    public void assignSwimClass(SwimClass swimClass) {
        this.swimClass = swimClass;
    }
}
