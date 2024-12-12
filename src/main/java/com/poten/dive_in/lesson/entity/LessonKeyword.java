package com.poten.dive_in.lesson.entity;


import com.poten.dive_in.cmmncode.entity.CommonCode;
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
    @JoinColumn(name = "cls_kywd_cd", referencedColumnName = "cd")
    private CommonCode keyword;

    @ManyToOne
    @JoinColumn(name = "cls_id")
    private SwimClass swimClass;

}
