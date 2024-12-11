package com.poten.dive_in.lesson.entity;


import com.poten.dive_in.cmmncode.entity.CmmnCd;
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
@Table(name = "swmm_cls_kywd_cd")
public class LessonKeyword {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cls_kywd_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cd_id")
    @Column(name = "cls_kywd_cd")
    private CmmnCd keyword;

    @ManyToOne
    @JoinColumn(name = "cls_id")
    @Column(name = "cls_id")
    private Lesson lesson;



}
