package com.poten.dive_in.entity;

import jakarta.persistence.*;

@Entity
public class LessonInstructor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="lesson_instructor_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "lesson_id")
    private Lesson lesson;

    @ManyToOne
    @JoinColumn(name="instructor_id")
    private Instructor instructor;

}
