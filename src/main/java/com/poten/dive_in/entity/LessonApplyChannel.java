package com.poten.dive_in.entity;

import com.poten.dive_in.enums.LessonChannelType;
import jakarta.persistence.*;

@Entity
public class LessonApplyChannel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lesson_link_id")
    private Long id;

    private String applyUrl;

    @Enumerated(EnumType.STRING)
    private LessonChannelType applyUrlType;

    @ManyToOne
    @JoinColumn(name = "lesson_id")
    private Lesson lesson;
}
