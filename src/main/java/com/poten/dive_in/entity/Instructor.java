package com.poten.dive_in.entity;

import jakarta.persistence.*;

@Entity
public class Instructor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "instructor_id")
    private Long id;

    private String instructorName;

    private String instructorInfo;

    @ManyToOne
    @JoinColumn(name = "academy_id" )
    private Academy academy;
}
