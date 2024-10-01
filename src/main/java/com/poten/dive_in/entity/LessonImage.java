package com.poten.dive_in.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.RequiredArgsConstructor;

@Builder
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
public class LessonImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lesson_image_id")
    private Long id;

    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "lesson_id")
    private Lesson lesson;
}
