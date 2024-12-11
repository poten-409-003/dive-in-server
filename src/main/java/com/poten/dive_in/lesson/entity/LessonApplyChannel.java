package com.poten.dive_in.lesson.entity;

import com.poten.dive_in.common.entity.BaseTimeEntity;
import com.poten.dive_in.lesson.enums.LessonChannelType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter @Builder
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "swmm_cls_appl_cd")
public class LessonApplyChannel extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "appl_id")
    private Long id;

    @Column(name = "appl_url")
    private String applyUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "appl_ty_cd")
    private LessonChannelType applyUrlType;

    @ManyToOne
    @JoinColumn(name = "cls_id")
    private Lesson lesson;
}
