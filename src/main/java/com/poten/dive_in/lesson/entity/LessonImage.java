package com.poten.dive_in.lesson.entity;

import com.poten.dive_in.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Builder @Getter
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "swmm_cls_img")
public class LessonImage extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "img_id")
    private Long id;

    private String imgUrl;

    @ManyToOne
    @JoinColumn(name = "cls_id")
    private Lesson lesson;

    private String rprsImgYn; //대표이미지여부
}
